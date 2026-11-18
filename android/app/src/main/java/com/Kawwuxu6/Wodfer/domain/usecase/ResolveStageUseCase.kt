package com.Kawwuxu6.Wodfer.domain.usecase

import com.Kawwuxu6.Wodfer.domain.model.Case
import com.Kawwuxu6.Wodfer.domain.model.StageKind
import com.Kawwuxu6.Wodfer.domain.model.StageOutcome

class ResolveStageUseCase {

    fun correctIndex(case: Case, stage: Int): Int = when (stageKind(stage)) {
        StageKind.WITNESS -> case.witnesses.indexOfFirst { it.contradicts }
        StageKind.CIPHER -> case.cipherAnswer
        StageKind.PROPS -> case.props.indexOfFirst { it.forged }
        StageKind.INTERROGATION -> case.questions.indexOfFirst { it.presses }
    }

    fun optionCount(case: Case, stage: Int): Int = when (stageKind(stage)) {
        StageKind.WITNESS -> case.witnesses.size
        StageKind.CIPHER -> case.cipherOptions.size
        StageKind.PROPS -> case.props.size
        StageKind.INTERROGATION -> case.questions.size
    }

    fun stageKind(stage: Int): StageKind = when (stage) {
        1 -> StageKind.WITNESS
        2 -> StageKind.CIPHER
        3 -> StageKind.PROPS
        else -> StageKind.INTERROGATION
    }

    operator fun invoke(case: Case, stage: Int, choice: Int): StageOutcome {
        val correct = choice == correctIndex(case, stage)
        val relation = when {
            stage < 4 -> if (correct) stage - 1 else -1
            correct -> 3
            choice % 2 == 0 -> 1
            else -> 2
        }
        return StageOutcome(
            correct = correct,
            evidenceSlot = stage - 1,
            revealedRelation = relation.takeIf { it in case.relations.indices } ?: -1
        )
    }
}
