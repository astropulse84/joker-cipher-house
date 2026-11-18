package com.Kawwuxu6.Wodfer.presentation.evidence

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kawwuxu6.Wodfer.core.config.GameConfig
import com.Kawwuxu6.Wodfer.data.session.CaseSessionHolder
import com.Kawwuxu6.Wodfer.domain.model.Case
import com.Kawwuxu6.Wodfer.domain.model.CaseProgress
import com.Kawwuxu6.Wodfer.domain.model.CaseResult
import com.Kawwuxu6.Wodfer.domain.model.Evidence
import com.Kawwuxu6.Wodfer.domain.model.EvidenceState
import com.Kawwuxu6.Wodfer.domain.model.Readability
import com.Kawwuxu6.Wodfer.domain.model.SuspectMark
import com.Kawwuxu6.Wodfer.domain.repository.CaseRepository
import com.Kawwuxu6.Wodfer.domain.repository.SettingsRepository
import com.Kawwuxu6.Wodfer.domain.usecase.GenerateCaseUseCase
import com.Kawwuxu6.Wodfer.domain.usecase.ResolveAccusationUseCase
import com.Kawwuxu6.Wodfer.domain.usecase.ScoreCaseUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EvidenceWallViewModel(
    private val caseRepository: CaseRepository,
    private val settingsRepository: SettingsRepository,
    private val session: CaseSessionHolder,
    private val generateCase: GenerateCaseUseCase,
    private val resolveAccusation: ResolveAccusationUseCase,
    private val scoreCase: ScoreCaseUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EvidenceWallUiState())
    val state: StateFlow<EvidenceWallUiState> = _state.asStateFlow()

    private val _events = Channel<WallEvent>(Channel.BUFFERED)
    val events: Flow<WallEvent> = _events.receiveAsFlow()

    private var current: Case? = null
    private var progress: CaseProgress? = null
    private var loadedSeed: Long = Long.MIN_VALUE

    init {
        viewModelScope.launch {
            session.progress.collect { value -> apply(value) }
        }
    }

    private fun apply(value: CaseProgress?) {
        val scale = if (settingsRepository.readability() == Readability.LARGE) {
            GameConfig.LARGE_TEXT_SCALE
        } else {
            1f
        }
        if (value == null) {
            val preview = generateCase.previewCase(0)
            current = preview
            progress = null
            loadedSeed = Long.MIN_VALUE
            _state.value = EvidenceWallUiState(
                ready = true,
                readOnly = true,
                chapterTitle = preview.chapterTitle,
                evidence = preview.evidenceNames.map { Evidence(it, EvidenceState.SEALED) },
                suspects = preview.suspects,
                relations = preview.relations,
                discovered = emptySet(),
                marks = List(preview.suspects.size) { SuspectMark.NONE },
                motives = preview.motives,
                selectedMotive = -1,
                focused = 0,
                composureTotal = GameConfig.COMPOSURE_INVESTIGATOR,
                composureLeft = GameConfig.COMPOSURE_INVESTIGATOR,
                textScale = scale
            )
            return
        }
        current = value.case
        progress = value
        val freshCase = value.case.seed != loadedSeed
        loadedSeed = value.case.seed
        _state.update { previous ->
            previous.copy(
                ready = true,
                readOnly = !value.active,
                chapterTitle = value.case.chapterTitle,
                evidence = value.evidence,
                suspects = value.case.suspects,
                relations = value.case.relations,
                discovered = value.discoveredRelations,
                marks = if (freshCase || previous.marks.size != value.case.suspects.size) {
                    List(value.case.suspects.size) { SuspectMark.NONE }
                } else {
                    previous.marks
                },
                motives = value.case.motives,
                selectedMotive = if (freshCase) -1 else previous.selectedMotive,
                focused = if (freshCase) 0 else previous.focused,
                composureTotal = value.composureTotal,
                composureLeft = value.composureLeft,
                textScale = scale
            )
        }
    }

    fun onNode(index: Int) {
        val snapshot = _state.value
        if (index !in snapshot.marks.indices) return
        _state.update { previous ->
            val next = when (previous.marks[index]) {
                SuspectMark.NONE -> SuspectMark.SUSPECT
                SuspectMark.SUSPECT -> SuspectMark.CLEARED
                SuspectMark.CLEARED -> SuspectMark.NONE
            }
            val updated = previous.marks.mapIndexed { position, mark ->
                when {
                    position == index -> next
                    next == SuspectMark.SUSPECT && mark == SuspectMark.SUSPECT -> SuspectMark.NONE
                    else -> mark
                }
            }
            previous.copy(marks = updated, focused = index)
        }
    }

    fun onMotive(index: Int) {
        _state.update { previous ->
            previous.copy(selectedMotive = if (previous.selectedMotive == index) -1 else index)
        }
    }

    fun onAccuse() {
        val case = current ?: return
        val active = progress ?: return
        val snapshot = _state.value
        if (snapshot.readOnly) return
        val accused = snapshot.marks.indexOfFirst { it == SuspectMark.SUSPECT }
        if (accused < 0 || snapshot.selectedMotive < 0) return

        val verdict = resolveAccusation(case, accused, snapshot.selectedMotive)
        val cleared = snapshot.marks
            .mapIndexedNotNull { position, mark ->
                position.takeIf { mark == SuspectMark.CLEARED }
            }
            .toSet()
        val clearedCorrect = resolveAccusation.clearedCorrect(case, cleared)
        val verified = snapshot.evidence.count { it.state == EvidenceState.VERIFIED }
        val score = scoreCase.score(verdict, snapshot.composureLeft, verified, clearedCorrect)
        val rank = scoreCase.rank(score)
        val unlocksNext = score >= GameConfig.UNLOCK_SCORE &&
            case.chapterIndex + 1 < GameConfig.CHAPTERS
        val result = CaseResult(
            chapterIndex = case.chapterIndex,
            chapterTitle = case.chapterTitle,
            verdict = verdict,
            rank = rank,
            score = score,
            composureLeft = snapshot.composureLeft,
            composureTotal = snapshot.composureTotal,
            verifiedEvidence = verified,
            reveal = case.reveal,
            seed = case.seed,
            nextChapterUnlocked = unlocksNext
        )
        caseRepository.record(result)
        session.publishProgress(active.copy(active = false))
        session.publishResult(result)
        viewModelScope.launch { _events.send(WallEvent.Finish(score)) }
    }
}
