package com.Kawwuxu6.Wodfer.presentation.casefiles

import androidx.lifecycle.ViewModel
import com.Kawwuxu6.Wodfer.core.config.GameConfig
import com.Kawwuxu6.Wodfer.data.session.CaseSessionHolder
import com.Kawwuxu6.Wodfer.domain.model.ChapterData
import com.Kawwuxu6.Wodfer.domain.model.ChapterSummary
import com.Kawwuxu6.Wodfer.domain.model.Readability
import com.Kawwuxu6.Wodfer.domain.repository.CaseRepository
import com.Kawwuxu6.Wodfer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CaseFilesViewModel(
    private val caseRepository: CaseRepository,
    private val settingsRepository: SettingsRepository,
    private val session: CaseSessionHolder,
    private val chapters: List<ChapterData>
) : ViewModel() {

    private val _state = MutableStateFlow(CaseFilesUiState())
    val state: StateFlow<CaseFilesUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val unlocked = caseRepository.unlockedChapters()
        val summaries = chapters.mapIndexed { index, chapter ->
            ChapterSummary(
                index = index,
                title = chapter.title,
                premise = chapter.premise,
                unlocked = index < unlocked,
                bestRank = caseRepository.bestRank(index),
                bestScore = caseRepository.bestScore(index)
            )
        }
        val scale = if (settingsRepository.readability() == Readability.LARGE) {
            GameConfig.LARGE_TEXT_SCALE
        } else {
            1f
        }
        _state.update {
            it.copy(
                chapters = summaries,
                history = caseRepository.history(),
                textScale = scale
            )
        }
    }

    fun openChapter(index: Int) {
        session.open(index)
    }
}
