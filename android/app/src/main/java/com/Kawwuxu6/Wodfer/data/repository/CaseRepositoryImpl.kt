package com.Kawwuxu6.Wodfer.data.repository

import com.Kawwuxu6.Wodfer.core.config.GameConfig
import com.Kawwuxu6.Wodfer.data.local.PreferencesStore
import com.Kawwuxu6.Wodfer.domain.model.CaseRecord
import com.Kawwuxu6.Wodfer.domain.model.CaseResult
import com.Kawwuxu6.Wodfer.domain.model.ChapterData
import com.Kawwuxu6.Wodfer.domain.model.Rank
import com.Kawwuxu6.Wodfer.domain.model.Verdict
import com.Kawwuxu6.Wodfer.domain.repository.CaseRepository

class CaseRepositoryImpl(
    private val store: PreferencesStore,
    private val chapters: List<ChapterData>
) : CaseRepository {

    override fun unlockedChapters(): Int =
        store.int(KEY_UNLOCKED, 1).coerceIn(1, GameConfig.CHAPTERS)

    override fun bestRank(chapterIndex: Int): Rank? {
        val ordinal = store.int("$KEY_BEST_RANK$chapterIndex", -1)
        return Rank.entries.getOrNull(ordinal)
    }

    override fun bestScore(chapterIndex: Int): Int = store.int("$KEY_BEST_SCORE$chapterIndex", 0)

    override fun casesClosed(): Int = store.int(KEY_CASES_CLOSED, 0)

    override fun trueVerdicts(): Int = store.int(KEY_TRUE_VERDICTS, 0)

    override fun bestOverallRank(): Rank? {
        var best: Rank? = null
        for (index in 0 until GameConfig.CHAPTERS) {
            val rank = bestRank(index) ?: continue
            if (best == null || rank.ordinal < best.ordinal) best = rank
        }
        return best
    }

    override fun history(): List<CaseRecord> {
        val count = store.int(KEY_HISTORY_COUNT, 0).coerceIn(0, GameConfig.HISTORY_MAX)
        val records = mutableListOf<CaseRecord>()
        for (slot in 0 until count) {
            val raw = store.string("$KEY_HISTORY$slot", "")
            parse(raw)?.let { records.add(it) }
        }
        return records
    }

    override fun record(result: CaseResult) {
        val count = store.int(KEY_HISTORY_COUNT, 0).coerceIn(0, GameConfig.HISTORY_MAX)
        val shifted = (count + 1).coerceAtMost(GameConfig.HISTORY_MAX)
        for (slot in shifted - 1 downTo 1) {
            store.putString("$KEY_HISTORY$slot", store.string("$KEY_HISTORY${slot - 1}", ""))
        }
        store.putString(
            "${KEY_HISTORY}0",
            listOf(
                result.chapterIndex,
                result.verdict.ordinal,
                result.rank.ordinal,
                result.score,
                result.composureLeft,
                result.seed,
                System.currentTimeMillis()
            ).joinToString(SEPARATOR)
        )
        store.putInt(KEY_HISTORY_COUNT, shifted)
        store.putInt(KEY_CASES_CLOSED, casesClosed() + 1)
        if (result.verdict == Verdict.TRUE_VERDICT) {
            store.putInt(KEY_TRUE_VERDICTS, trueVerdicts() + 1)
        }
        val previousRank = bestRank(result.chapterIndex)
        if (previousRank == null || result.rank.ordinal < previousRank.ordinal) {
            store.putInt("$KEY_BEST_RANK${result.chapterIndex}", result.rank.ordinal)
        }
        if (result.score > bestScore(result.chapterIndex)) {
            store.putInt("$KEY_BEST_SCORE${result.chapterIndex}", result.score)
        }
        if (result.score >= GameConfig.UNLOCK_SCORE) {
            val next = (result.chapterIndex + 2).coerceAtMost(GameConfig.CHAPTERS)
            if (next > unlockedChapters()) store.putInt(KEY_UNLOCKED, next)
        }
    }

    override fun clearHistory() {
        val keys = (0 until GameConfig.HISTORY_MAX).map { "$KEY_HISTORY$it" }
        store.drop(keys)
        store.putInt(KEY_HISTORY_COUNT, 0)
        store.putInt(KEY_CASES_CLOSED, 0)
        store.putInt(KEY_TRUE_VERDICTS, 0)
    }

    private fun parse(raw: String): CaseRecord? {
        if (raw.isEmpty()) return null
        val parts = raw.split(SEPARATOR)
        if (parts.size < 7) return null
        val chapterIndex = parts[0].toIntOrNull() ?: return null
        val verdict = Verdict.entries.getOrNull(parts[1].toIntOrNull() ?: -1) ?: return null
        val rank = Rank.entries.getOrNull(parts[2].toIntOrNull() ?: -1) ?: return null
        return CaseRecord(
            chapterIndex = chapterIndex,
            chapterTitle = chapters.getOrNull(chapterIndex)?.title ?: "",
            verdict = verdict,
            rank = rank,
            score = parts[3].toIntOrNull() ?: 0,
            composureLeft = parts[4].toIntOrNull() ?: 0,
            seed = parts[5].toLongOrNull() ?: 0L,
            epochMillis = parts[6].toLongOrNull() ?: 0L
        )
    }

    private companion object {
        const val SEPARATOR = "|"
        const val KEY_UNLOCKED = "unlocked_chapters"
        const val KEY_BEST_RANK = "best_rank_c"
        const val KEY_BEST_SCORE = "best_score_c"
        const val KEY_CASES_CLOSED = "cases_closed"
        const val KEY_TRUE_VERDICTS = "true_verdicts"
        const val KEY_HISTORY_COUNT = "history_count"
        const val KEY_HISTORY = "history_"
    }
}
