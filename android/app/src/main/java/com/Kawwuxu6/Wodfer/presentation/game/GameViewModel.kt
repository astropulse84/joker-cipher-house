package com.Kawwuxu6.Wodfer.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kawwuxu6.Wodfer.core.config.GameConfig
import com.Kawwuxu6.Wodfer.data.session.CaseSessionHolder
import com.Kawwuxu6.Wodfer.domain.model.Case
import com.Kawwuxu6.Wodfer.domain.model.CaseProgress
import com.Kawwuxu6.Wodfer.domain.model.CaseRequest
import com.Kawwuxu6.Wodfer.domain.model.CaseResult
import com.Kawwuxu6.Wodfer.domain.model.Difficulty
import com.Kawwuxu6.Wodfer.domain.model.Evidence
import com.Kawwuxu6.Wodfer.domain.model.EvidenceState
import com.Kawwuxu6.Wodfer.domain.model.Readability
import com.Kawwuxu6.Wodfer.domain.model.StageKind
import com.Kawwuxu6.Wodfer.domain.model.Verdict
import com.Kawwuxu6.Wodfer.domain.repository.CaseRepository
import com.Kawwuxu6.Wodfer.domain.repository.SettingsRepository
import com.Kawwuxu6.Wodfer.domain.usecase.GenerateCaseUseCase
import com.Kawwuxu6.Wodfer.domain.usecase.ResolveStageUseCase
import com.Kawwuxu6.Wodfer.domain.usecase.ScoreCaseUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val caseRepository: CaseRepository,
    private val settingsRepository: SettingsRepository,
    private val session: CaseSessionHolder,
    private val generateCase: GenerateCaseUseCase,
    private val resolveStage: ResolveStageUseCase,
    private val scoreCase: ScoreCaseUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = _events.receiveAsFlow()

    private var current: Case? = null
    private var evidence: MutableList<Evidence> = mutableListOf()
    private var discovered: MutableSet<Int> = mutableSetOf()
    private var lastRequest: CaseRequest? = null
    private var stampJob: Job? = null
    private var legendJob: Job? = null
    private var collapseJob: Job? = null

    init {
        viewModelScope.launch {
            session.request.collect { request ->
                if (request != null && request != lastRequest) {
                    lastRequest = request
                    prepare(request)
                }
            }
        }
    }

    private fun prepare(request: CaseRequest) {
        cancelJobs()
        val case = generateCase(request.chapterIndex, request.attempt)
        current = case
        evidence = case.evidenceNames
            .map { Evidence(it, EvidenceState.SEALED) }
            .toMutableList()
        discovered = mutableSetOf()
        val total = if (settingsRepository.difficulty() == Difficulty.INSPECTOR) {
            GameConfig.COMPOSURE_INSPECTOR
        } else {
            GameConfig.COMPOSURE_INVESTIGATOR
        }
        val scale = if (settingsRepository.readability() == Readability.LARGE) {
            GameConfig.LARGE_TEXT_SCALE
        } else {
            1f
        }
        _state.value = GameUiState(
            ready = true,
            chapterIndex = case.chapterIndex,
            chapterTitle = case.chapterTitle,
            stage = 1,
            stageKind = StageKind.WITNESS,
            houseRecord = case.houseRecord,
            witnesses = case.witnesses,
            cards = case.cards,
            cipherOptions = case.cipherOptions,
            genuineMark = case.genuineMark,
            props = case.props,
            targetName = case.targetName,
            targetRole = case.targetRole,
            questions = case.questions,
            composureTotal = total,
            composureLeft = total,
            fragments = 0,
            evidenceVerified = 0,
            correctIndex = resolveStage.correctIndex(case, 1),
            legendVisible = settingsRepository.legendPinned(),
            inspectAllowed = settingsRepository.difficulty() != Difficulty.INSPECTOR,
            textScale = scale
        )
        publishProgress(active = true)
    }

    fun onClue(index: Int) {
        val case = current ?: return
        val snapshot = _state.value
        if (!snapshot.ready || snapshot.paused) return
        if (snapshot.phase != GamePhase.IDLE || index == snapshot.struck) return

        val outcome = resolveStage(case, snapshot.stage, index)
        val slot = outcome.evidenceSlot
        if (slot in evidence.indices) {
            evidence[slot] = evidence[slot].copy(
                state = if (outcome.correct) EvidenceState.VERIFIED else EvidenceState.UNVERIFIED
            )
        }
        if (outcome.revealedRelation >= 0) discovered.add(outcome.revealedRelation)

        val composure = if (outcome.correct) {
            snapshot.composureLeft
        } else {
            (snapshot.composureLeft - 1).coerceAtLeast(0)
        }
        val fragments = if (outcome.correct) snapshot.fragments + 1 else snapshot.fragments
        _state.update {
            it.copy(
                chosen = index,
                phase = GamePhase.RESOLVED,
                stampVisible = true,
                stampCorrect = outcome.correct,
                composureLeft = composure,
                fragments = fragments,
                evidenceVerified = evidence.count { slotItem ->
                    slotItem.state == EvidenceState.VERIFIED
                }
            )
        }
        publishProgress(active = true)

        stampJob?.cancel()
        stampJob = viewModelScope.launch {
            delay(GameConfig.STAMP_HOLD_MS)
            _state.update { it.copy(stampVisible = false) }
        }

        if (composure <= 0) {
            collapseJob?.cancel()
            collapseJob = viewModelScope.launch {
                delay(GameConfig.COLLAPSE_DELAY_MS)
                finishCollapsed()
            }
        }
    }

    fun onAdvance() {
        val case = current ?: return
        val snapshot = _state.value
        if (snapshot.phase != GamePhase.RESOLVED || snapshot.composureLeft <= 0) return
        if (snapshot.stage >= GameConfig.STAGES) {
            viewModelScope.launch { _events.send(GameEvent.OpenWall) }
            return
        }
        val nextStage = snapshot.stage + 1
        stampJob?.cancel()
        _state.update {
            it.copy(
                stage = nextStage,
                stageKind = resolveStage.stageKind(nextStage),
                chosen = -1,
                struck = -1,
                phase = GamePhase.IDLE,
                stampVisible = false,
                correctIndex = resolveStage.correctIndex(case, nextStage),
                legendVisible = settingsRepository.legendPinned() || nextStage == 2
            )
        }
        if (nextStage == 2 && !settingsRepository.legendPinned()) {
            legendJob?.cancel()
            legendJob = viewModelScope.launch {
                delay(GameConfig.LEGEND_HOLD_MS)
                _state.update { it.copy(legendVisible = false) }
            }
        }
    }

    fun onInspect() {
        val case = current ?: return
        val snapshot = _state.value
        if (!snapshot.inspectAllowed || snapshot.phase != GamePhase.IDLE) return
        if (snapshot.fragments < GameConfig.INSPECT_COST || snapshot.struck >= 0) return
        val correct = resolveStage.correctIndex(case, snapshot.stage)
        val options = resolveStage.optionCount(case, snapshot.stage)
        val candidate = (0 until options).firstOrNull { it != correct } ?: return
        _state.update {
            it.copy(
                struck = candidate,
                fragments = it.fragments - GameConfig.INSPECT_COST,
                legendVisible = true
            )
        }
        publishProgress(active = true)
    }

    fun onPause() {
        _state.update { it.copy(paused = true) }
    }

    fun onResume() {
        _state.update { it.copy(paused = false) }
    }

    fun onLeave() {
        cancelJobs()
        _state.update { it.copy(paused = false, stampVisible = false) }
    }

    private fun cancelJobs() {
        stampJob?.cancel()
        legendJob?.cancel()
        collapseJob?.cancel()
    }

    private fun finishCollapsed() {
        val case = current ?: return
        val snapshot = _state.value
        val verified = evidence.count { it.state == EvidenceState.VERIFIED }
        val score = scoreCase.score(Verdict.CASE_COLLAPSED, 0, verified, 0)
        val rank = scoreCase.rank(score)
        val result = CaseResult(
            chapterIndex = case.chapterIndex,
            chapterTitle = case.chapterTitle,
            verdict = Verdict.CASE_COLLAPSED,
            rank = rank,
            score = score,
            composureLeft = 0,
            composureTotal = snapshot.composureTotal,
            verifiedEvidence = verified,
            reveal = case.reveal,
            seed = case.seed,
            nextChapterUnlocked = false
        )
        caseRepository.record(result)
        publishProgress(active = false)
        session.publishResult(result)
        viewModelScope.launch { _events.send(GameEvent.Finish(score)) }
    }

    private fun publishProgress(active: Boolean) {
        val case = current ?: return
        val snapshot = _state.value
        session.publishProgress(
            CaseProgress(
                case = case,
                evidence = evidence.toList(),
                discoveredRelations = discovered.toSet(),
                composureTotal = snapshot.composureTotal,
                composureLeft = snapshot.composureLeft,
                fragments = snapshot.fragments,
                active = active
            )
        )
    }
}
