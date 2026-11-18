package com.Kawwuxu6.Wodfer.domain.usecase

import com.Kawwuxu6.Wodfer.core.config.GameConfig
import com.Kawwuxu6.Wodfer.domain.model.Rank
import com.Kawwuxu6.Wodfer.domain.model.Verdict

class ScoreCaseUseCase {

    fun score(
        verdict: Verdict,
        composureLeft: Int,
        verifiedEvidence: Int,
        clearedCorrect: Int
    ): Int {
        val base = when (verdict) {
            Verdict.TRUE_VERDICT -> 60
            Verdict.HALF_TRUTH -> 35
            Verdict.FALSE_HAND -> 10
            Verdict.CASE_COLLAPSED -> 0
        }
        return base +
            composureLeft * GameConfig.COMPOSURE_BONUS +
            verifiedEvidence * GameConfig.EVIDENCE_BONUS +
            clearedCorrect * GameConfig.CLEARED_BONUS
    }

    fun rank(score: Int): Rank = when {
        score >= 95 -> Rank.RUBY_SEAL
        score >= 65 -> Rank.GOLD_LEAF
        score >= 30 -> Rank.IVORY_NOTE
        else -> Rank.ASH
    }
}
