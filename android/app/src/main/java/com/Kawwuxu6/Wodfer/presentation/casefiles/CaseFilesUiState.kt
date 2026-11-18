package com.Kawwuxu6.Wodfer.presentation.casefiles

import com.Kawwuxu6.Wodfer.domain.model.CaseRecord
import com.Kawwuxu6.Wodfer.domain.model.ChapterSummary

data class CaseFilesUiState(
    val chapters: List<ChapterSummary> = emptyList(),
    val history: List<CaseRecord> = emptyList(),
    val textScale: Float = 1f
)
