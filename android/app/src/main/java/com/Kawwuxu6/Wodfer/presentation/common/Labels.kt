package com.Kawwuxu6.Wodfer.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.Kawwuxu6.Wodfer.R
import com.Kawwuxu6.Wodfer.core.theme.Ash
import com.Kawwuxu6.Wodfer.core.theme.GoldLight
import com.Kawwuxu6.Wodfer.core.theme.GoldLine
import com.Kawwuxu6.Wodfer.core.theme.Ivory
import com.Kawwuxu6.Wodfer.core.theme.RubyBright
import com.Kawwuxu6.Wodfer.domain.model.Rank
import com.Kawwuxu6.Wodfer.domain.model.StageKind
import com.Kawwuxu6.Wodfer.domain.model.Verdict

@Composable
fun rankText(rank: Rank): String = stringResource(
    when (rank) {
        Rank.RUBY_SEAL -> R.string.rank_ruby
        Rank.GOLD_LEAF -> R.string.rank_gold
        Rank.IVORY_NOTE -> R.string.rank_ivory
        Rank.ASH -> R.string.rank_ash
    }
)

fun rankAccent(rank: Rank): Color = when (rank) {
    Rank.RUBY_SEAL -> RubyBright
    Rank.GOLD_LEAF -> GoldLine
    Rank.IVORY_NOTE -> Ivory
    Rank.ASH -> Ash
}

@Composable
fun verdictWord(verdict: Verdict): String = stringResource(
    when (verdict) {
        Verdict.TRUE_VERDICT -> R.string.verdict_true
        Verdict.HALF_TRUTH -> R.string.verdict_half
        Verdict.FALSE_HAND -> R.string.verdict_false
        Verdict.CASE_COLLAPSED -> R.string.verdict_collapsed
    }
)

@Composable
fun verdictHeadline(verdict: Verdict): String = stringResource(
    when (verdict) {
        Verdict.TRUE_VERDICT -> R.string.verdict_true_line
        Verdict.HALF_TRUTH -> R.string.verdict_half_line
        Verdict.FALSE_HAND -> R.string.verdict_false_line
        Verdict.CASE_COLLAPSED -> R.string.verdict_collapsed_line
    }
)

fun verdictAccent(verdict: Verdict): Color = when (verdict) {
    Verdict.TRUE_VERDICT -> GoldLight
    Verdict.HALF_TRUTH -> GoldLine
    Verdict.FALSE_HAND -> Ash
    Verdict.CASE_COLLAPSED -> RubyBright
}

@Composable
fun chapterNumeral(index: Int): String = stringResource(
    when (index) {
        0 -> R.string.chapter_numeral_1
        1 -> R.string.chapter_numeral_2
        2 -> R.string.chapter_numeral_3
        3 -> R.string.chapter_numeral_4
        else -> R.string.chapter_numeral_5
    }
)

@Composable
fun stageTitle(kind: StageKind): String = stringResource(
    when (kind) {
        StageKind.WITNESS -> R.string.stage_witness
        StageKind.CIPHER -> R.string.stage_cipher
        StageKind.PROPS -> R.string.stage_props
        StageKind.INTERROGATION -> R.string.stage_interrogation
    }
)
