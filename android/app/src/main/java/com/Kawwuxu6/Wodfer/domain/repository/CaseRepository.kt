package com.Kawwuxu6.Wodfer.domain.repository

import com.Kawwuxu6.Wodfer.domain.model.CaseRecord
import com.Kawwuxu6.Wodfer.domain.model.CaseResult
import com.Kawwuxu6.Wodfer.domain.model.Rank

interface CaseRepository {
    fun unlockedChapters(): Int
    fun bestRank(chapterIndex: Int): Rank?
    fun bestScore(chapterIndex: Int): Int
    fun casesClosed(): Int
    fun trueVerdicts(): Int
    fun bestOverallRank(): Rank?
    fun history(): List<CaseRecord>
    fun record(result: CaseResult)
    fun clearHistory()
}
