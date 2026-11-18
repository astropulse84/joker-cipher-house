package com.Kawwuxu6.Wodfer.presentation.menu

import androidx.lifecycle.ViewModel
import com.Kawwuxu6.Wodfer.data.session.CaseSessionHolder
import com.Kawwuxu6.Wodfer.domain.repository.CaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MenuViewModel(
    private val caseRepository: CaseRepository,
    private val session: CaseSessionHolder
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _state.update {
            it.copy(
                casesClosed = caseRepository.casesClosed(),
                trueVerdicts = caseRepository.trueVerdicts(),
                bestRank = caseRepository.bestOverallRank(),
                unlockedChapters = caseRepository.unlockedChapters()
            )
        }
    }

    fun openLatestCase() {
        session.open((caseRepository.unlockedChapters() - 1).coerceAtLeast(0))
    }
}
