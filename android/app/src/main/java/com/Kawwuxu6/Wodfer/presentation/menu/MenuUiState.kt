package com.Kawwuxu6.Wodfer.presentation.menu

import com.Kawwuxu6.Wodfer.domain.model.Rank

data class MenuUiState(
    val casesClosed: Int = 0,
    val trueVerdicts: Int = 0,
    val bestRank: Rank? = null,
    val unlockedChapters: Int = 1
)
