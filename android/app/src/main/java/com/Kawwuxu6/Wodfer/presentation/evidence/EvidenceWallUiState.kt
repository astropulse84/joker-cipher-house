package com.Kawwuxu6.Wodfer.presentation.evidence

import com.Kawwuxu6.Wodfer.domain.model.Evidence
import com.Kawwuxu6.Wodfer.domain.model.Relation
import com.Kawwuxu6.Wodfer.domain.model.Suspect
import com.Kawwuxu6.Wodfer.domain.model.SuspectMark

data class EvidenceWallUiState(
    val ready: Boolean = false,
    val readOnly: Boolean = true,
    val chapterTitle: String = "",
    val evidence: List<Evidence> = emptyList(),
    val suspects: List<Suspect> = emptyList(),
    val relations: List<Relation> = emptyList(),
    val discovered: Set<Int> = emptySet(),
    val marks: List<SuspectMark> = emptyList(),
    val motives: List<String> = emptyList(),
    val selectedMotive: Int = -1,
    val focused: Int = 0,
    val composureTotal: Int = 3,
    val composureLeft: Int = 3,
    val textScale: Float = 1f
)

sealed interface WallEvent {
    data class Finish(val score: Int) : WallEvent
}
