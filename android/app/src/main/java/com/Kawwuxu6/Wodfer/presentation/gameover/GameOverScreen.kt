package com.Kawwuxu6.Wodfer.presentation.gameover

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kawwuxu6.Wodfer.R
import com.Kawwuxu6.Wodfer.core.di.ServiceLocator
import com.Kawwuxu6.Wodfer.core.theme.GoldLight
import com.Kawwuxu6.Wodfer.core.theme.GoldLine
import com.Kawwuxu6.Wodfer.core.theme.InkBlack
import com.Kawwuxu6.Wodfer.core.theme.Ivory
import com.Kawwuxu6.Wodfer.core.theme.IvoryDim
import com.Kawwuxu6.Wodfer.core.theme.RubyBright
import com.Kawwuxu6.Wodfer.core.theme.RubyDeep
import com.Kawwuxu6.Wodfer.core.theme.VerdictAsh
import com.Kawwuxu6.Wodfer.core.theme.VelvetCard
import com.Kawwuxu6.Wodfer.core.ui.EmptyNote
import com.Kawwuxu6.Wodfer.core.ui.PrimaryButton
import com.Kawwuxu6.Wodfer.core.ui.ScreenBackground
import com.Kawwuxu6.Wodfer.core.ui.SealBadge
import com.Kawwuxu6.Wodfer.core.ui.SecondaryButton
import com.Kawwuxu6.Wodfer.core.ui.StatCard
import com.Kawwuxu6.Wodfer.domain.model.CaseResult
import com.Kawwuxu6.Wodfer.domain.model.Verdict
import com.Kawwuxu6.Wodfer.presentation.common.rankAccent
import com.Kawwuxu6.Wodfer.presentation.common.rankText
import com.Kawwuxu6.Wodfer.presentation.common.verdictAccent
import com.Kawwuxu6.Wodfer.presentation.common.verdictHeadline

@Composable
fun GameOverScreen(
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: GameOverViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is GameOverEvent.OpenCase -> onPlayAgain()
            }
        }
    }

    val result = state.result
    val verdict = result?.verdict ?: Verdict.FALSE_HAND
    val lost = verdict == Verdict.FALSE_HAND || verdict == Verdict.CASE_COLLAPSED

    ScreenBackground(
        modifier = modifier,
        tones = listOf(
            InkBlack.copy(alpha = 0.92f),
            when (verdict) {
                Verdict.TRUE_VERDICT -> GoldLine.copy(alpha = 0.16f)
                Verdict.HALF_TRUTH -> GoldLine.copy(alpha = 0.10f)
                Verdict.FALSE_HAND -> VerdictAsh.copy(alpha = 0.9f)
                Verdict.CASE_COLLAPSED -> RubyDeep.copy(alpha = 0.55f)
            }
        ),
        imageAlpha = 0.3f,
        lampX = 0.5f,
        lampY = 0.26f,
        lampAlpha = if (lost) 0.06f else 0.14f,
        lampTint = if (lost) RubyBright else GoldLight
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (result == null) {
                EmptyNote(text = stringResource(R.string.wall_empty))
                SecondaryButton(
                    text = stringResource(R.string.menu),
                    onClick = onMenu,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    leadingIcon = R.drawable.icon_home
                )
                return@Column
            }
            VerdictSeal(verdict = verdict, lost = lost)
            Text(
                text = verdictHeadline(verdict),
                style = MaterialTheme.typography.displaySmall,
                color = Ivory,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 18.dp)
            )
            RevealCard(result = result)
            ResultStats(result = result)
            PrimaryButton(
                text = stringResource(R.string.play_again),
                onClick = viewModel::replay,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                leadingIcon = R.drawable.ic_seal
            )
            if (state.nextAvailable) {
                SecondaryButton(
                    text = stringResource(R.string.next_case),
                    onClick = viewModel::nextCase,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    leadingIcon = R.drawable.ic_folder
                )
            }
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                leadingIcon = R.drawable.icon_home,
                height = 48.dp
            )
        }
    }
}

@Composable
private fun VerdictSeal(
    verdict: Verdict,
    lost: Boolean,
    modifier: Modifier = Modifier
) {
    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(verdict) {
        revealed = true
    }
    val grow by animateFloatAsState(
        targetValue = if (revealed) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 150f)
    )
    val sweep by animateFloatAsState(
        targetValue = if (revealed) 1f else 0f,
        animationSpec = tween(900)
    )
    SealBadge(
        accent = verdictAccent(verdict),
        modifier = modifier.graphicsLayer {
            val scale = 0.72f + 0.28f * grow
            scaleX = scale
            scaleY = scale
            alpha = grow
        },
        size = 112.dp,
        ringSweep = sweep,
        cracked = lost
    )
}

@Composable
private fun RevealCard(
    result: CaseResult,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(shape)
            .background(VelvetCard.copy(alpha = 0.9f))
            .border(1.dp, RubyDeep.copy(alpha = 0.7f), shape)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_4),
            contentDescription = stringResource(R.string.cd_dossier),
            modifier = Modifier.size(48.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = result.reveal,
            style = MaterialTheme.typography.bodyLarge,
            color = Ivory,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
private fun ResultStats(
    result: CaseResult,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard(
            label = stringResource(R.string.stat_rank),
            value = rankText(result.rank),
            accent = rankAccent(result.rank),
            modifier = Modifier.weight(1f),
            height = 82.dp
        )
        StatCard(
            label = stringResource(R.string.stat_score),
            value = stringResource(R.string.score_value, result.score),
            accent = GoldLight,
            modifier = Modifier.weight(1f),
            height = 82.dp
        )
        StatCard(
            label = stringResource(R.string.stat_composure),
            value = stringResource(
                R.string.composure_value,
                result.composureLeft,
                result.composureTotal
            ),
            accent = if (result.composureLeft > 0) IvoryDim else RubyBright,
            modifier = Modifier.weight(1f),
            height = 82.dp
        )
    }
}
