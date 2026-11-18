package com.Kawwuxu6.Wodfer.presentation.settings

import com.Kawwuxu6.Wodfer.domain.model.Difficulty
import com.Kawwuxu6.Wodfer.domain.model.Readability

data class SettingsUiState(
    val readability: Readability = Readability.STANDARD,
    val difficulty: Difficulty = Difficulty.INVESTIGATOR,
    val legendPinned: Boolean = true,
    val historyCount: Int = 0,
    val confirmClear: Boolean = false
)
