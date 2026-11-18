package com.Kawwuxu6.Wodfer.domain.model

data class CaseRecord(
    val chapterIndex: Int,
    val chapterTitle: String,
    val verdict: Verdict,
    val rank: Rank,
    val score: Int,
    val composureLeft: Int,
    val seed: Long,
    val epochMillis: Long
)

data class CaseRequest(
    val chapterIndex: Int,
    val attempt: Int
)

data class CaseProgress(
    val case: Case,
    val evidence: List<Evidence>,
    val discoveredRelations: Set<Int>,
    val composureTotal: Int,
    val composureLeft: Int,
    val fragments: Int,
    val active: Boolean
)

data class CaseResult(
    val chapterIndex: Int,
    val chapterTitle: String,
    val verdict: Verdict,
    val rank: Rank,
    val score: Int,
    val composureLeft: Int,
    val composureTotal: Int,
    val verifiedEvidence: Int,
    val reveal: String,
    val seed: Long,
    val nextChapterUnlocked: Boolean
)

data class ChapterSummary(
    val index: Int,
    val title: String,
    val premise: String,
    val unlocked: Boolean,
    val bestRank: Rank?,
    val bestScore: Int
)
