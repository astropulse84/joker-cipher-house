package com.Kawwuxu6.Wodfer.domain.usecase

import com.Kawwuxu6.Wodfer.domain.model.Case
import com.Kawwuxu6.Wodfer.domain.model.Verdict

class ResolveAccusationUseCase {

    operator fun invoke(case: Case, accused: Int, motive: Int): Verdict {
        val rightHand = accused == case.trueSuspect
        val rightReason = motive == case.trueMotive
        return when {
            rightHand && rightReason -> Verdict.TRUE_VERDICT
            rightHand -> Verdict.HALF_TRUTH
            else -> Verdict.FALSE_HAND
        }
    }

    fun clearedCorrect(case: Case, cleared: Set<Int>): Int =
        cleared.count { it != case.trueSuspect && it in case.suspects.indices }
}
