package com.Kawwuxu6.Wodfer.presentation.game

import com.Kawwuxu6.Wodfer.domain.model.CipherCard
import com.Kawwuxu6.Wodfer.domain.model.PropClue
import com.Kawwuxu6.Wodfer.domain.model.QuestionClue
import com.Kawwuxu6.Wodfer.domain.model.StageKind
import com.Kawwuxu6.Wodfer.domain.model.WitnessClue

enum class GamePhase { IDLE, RESOLVED }

data class GameUiState(
    val ready: Boolean = false,
    val chapterIndex: Int = 0,
    val chapterTitle: String = "",
    val stage: Int = 1,
    val stageKind: StageKind = StageKind.WITNESS,
    val houseRecord: String = "",
    val witnesses: List<WitnessClue> = emptyList(),
    val cards: List<CipherCard> = emptyList(),
    val cipherOptions: List<String> = emptyList(),
    val genuineMark: String = "",
    val props: List<PropClue> = emptyList(),
    val targetName: String = "",
    val targetRole: String = "",
    val questions: List<QuestionClue> = emptyList(),
    val composureTotal: Int = 3,
    val composureLeft: Int = 3,
    val fragments: Int = 0,
    val evidenceVerified: Int = 0,
    val chosen: Int = -1,
    val correctIndex: Int = -1,
    val struck: Int = -1,
    val phase: GamePhase = GamePhase.IDLE,
    val stampVisible: Boolean = false,
    val stampCorrect: Boolean = false,
    val legendVisible: Boolean = true,
    val inspectAllowed: Boolean = true,
    val textScale: Float = 1f,
    val paused: Boolean = false
)

sealed interface GameEvent {
    data object OpenWall : GameEvent
    data class Finish(val score: Int) : GameEvent
}
