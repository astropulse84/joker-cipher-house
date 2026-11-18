package com.Kawwuxu6.Wodfer.presentation.settings

import androidx.lifecycle.ViewModel
import com.Kawwuxu6.Wodfer.domain.model.Difficulty
import com.Kawwuxu6.Wodfer.domain.model.Readability
import com.Kawwuxu6.Wodfer.domain.repository.CaseRepository
import com.Kawwuxu6.Wodfer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val caseRepository: CaseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        reload()
    }

    fun reload() {
        _state.update {
            it.copy(
                readability = settingsRepository.readability(),
                difficulty = settingsRepository.difficulty(),
                legendPinned = settingsRepository.legendPinned(),
                historyCount = caseRepository.history().size
            )
        }
    }

    fun setReadability(value: Readability) {
        settingsRepository.setReadability(value)
        _state.update { it.copy(readability = value) }
    }

    fun setDifficulty(value: Difficulty) {
        settingsRepository.setDifficulty(value)
        _state.update { it.copy(difficulty = value) }
    }

    fun setLegend(value: Boolean) {
        settingsRepository.setLegendPinned(value)
        _state.update { it.copy(legendPinned = value) }
    }

    fun askClear() {
        _state.update { it.copy(confirmClear = true) }
    }

    fun cancelClear() {
        _state.update { it.copy(confirmClear = false) }
    }

    fun confirmClear() {
        caseRepository.clearHistory()
        _state.update { it.copy(confirmClear = false, historyCount = 0) }
    }
}
