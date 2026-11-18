package com.Kawwuxu6.Wodfer.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kawwuxu6.Wodfer.R
import com.Kawwuxu6.Wodfer.core.di.ServiceLocator
import com.Kawwuxu6.Wodfer.core.theme.Ash
import com.Kawwuxu6.Wodfer.core.theme.GoldDim
import com.Kawwuxu6.Wodfer.core.theme.GoldLight
import com.Kawwuxu6.Wodfer.core.theme.GoldLine
import com.Kawwuxu6.Wodfer.core.theme.InkBlack
import com.Kawwuxu6.Wodfer.core.theme.Ivory
import com.Kawwuxu6.Wodfer.core.theme.IvoryDim
import com.Kawwuxu6.Wodfer.core.theme.RubyBright
import com.Kawwuxu6.Wodfer.core.theme.RubyDeep
import com.Kawwuxu6.Wodfer.core.theme.VelvetDeep
import com.Kawwuxu6.Wodfer.core.theme.VelvetGlow
import com.Kawwuxu6.Wodfer.core.ui.PrimaryButton
import com.Kawwuxu6.Wodfer.core.ui.ScreenBackground
import com.Kawwuxu6.Wodfer.core.ui.SecondaryButton
import com.Kawwuxu6.Wodfer.core.ui.StatCard
import com.Kawwuxu6.Wodfer.presentation.common.chapterNumeral
import com.Kawwuxu6.Wodfer.presentation.common.rankAccent
import com.Kawwuxu6.Wodfer.presentation.common.rankText

@Composable
fun MenuScreen(
    onPlay: () -> Unit,
    onCaseFiles: () -> Unit,
    onEvidenceWall: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: MenuViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        appeared = true
    }
    val entry by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(460, easing = FastOutSlowInEasing)
    )

    ScreenBackground(
        modifier = modifier,
        tones = listOf(
            InkBlack.copy(alpha = 0.55f),
            InkBlack.copy(alpha = 0.10f),
            VelvetGlow.copy(alpha = 0.95f)
        ),
        imageAlpha = 0.72f,
        lampX = 0.5f,
        lampY = 0.18f,
        lampAlpha = 0.12f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ArtBand(modifier = Modifier.weight(1f))
            MenuSheet(
                state = state,
                entry = entry,
                onPlay = {
                    viewModel.openLatestCase()
                    onPlay()
                },
                onCaseFiles = onCaseFiles,
                onEvidenceWall = onEvidenceWall,
                onSettings = onSettings
            )
        }
    }
}

@Composable
private fun ArtBand(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 18.dp, top = 14.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(InkBlack.copy(alpha = 0.7f))
                .border(1.dp, GoldLine.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(
                text = stringResource(R.string.chip_age),
                style = MaterialTheme.typography.labelSmall,
                color = IvoryDim
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .rotate(45f)
                    .border(1.5.dp, GoldLine.copy(alpha = 0.75f), RoundedCornerShape(10.dp))
            )
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.cd_logo),
                modifier = Modifier.size(104.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun MenuSheet(
    state: MenuUiState,
    entry: Float,
    onPlay: () -> Unit,
    onCaseFiles: () -> Unit,
    onEvidenceWall: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationY = (1f - entry) * 90f
                alpha = entry
            },
        shape = shape,
        color = VelvetDeep.copy(alpha = 0.97f),
        shadowElevation = 18.dp,
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.horizontalGradient(
                listOf(
                    GoldLine.copy(alpha = 0.55f),
                    RubyDeep.copy(alpha = 0.4f),
                    GoldLine.copy(alpha = 0.55f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .width(44.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(GoldLine.copy(alpha = 0.6f))
            )
            Text(
                text = stringResource(R.string.title_app),
                style = MaterialTheme.typography.displayLarge,
                color = Ivory,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 14.dp)
            )
            Text(
                text = stringResource(R.string.menu_tagline),
                style = MaterialTheme.typography.labelSmall,
                color = IvoryDim,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            MenuStats(
                state = state,
                modifier = Modifier.padding(top = 14.dp)
            )
            Text(
                text = stringResource(
                    R.string.menu_chapter_hint,
                    chapterNumeral(state.unlockedChapters - 1)
                ),
                style = MaterialTheme.typography.labelSmall,
                color = GoldDim,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 14.dp, bottom = 10.dp)
            )
            PrimaryButton(
                text = stringResource(R.string.play),
                onClick = onPlay,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = R.drawable.ic_seal,
                height = 60.dp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.case_files),
                    onClick = onCaseFiles,
                    modifier = Modifier.weight(1f),
                    leadingIcon = R.drawable.ic_folder
                )
                SecondaryButton(
                    text = stringResource(R.string.evidence_wall),
                    onClick = onEvidenceWall,
                    modifier = Modifier.weight(1f),
                    leadingIcon = R.drawable.ic_wall
                )
            }
            SecondaryButton(
                text = stringResource(R.string.settings),
                onClick = onSettings,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                leadingIcon = R.drawable.ic_gear,
                height = 48.dp
            )
            Text(
                text = stringResource(R.string.menu_footer),
                style = MaterialTheme.typography.labelSmall,
                color = Ash,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 14.dp, bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun MenuStats(
    state: MenuUiState,
    modifier: Modifier = Modifier
) {
    val rank = state.bestRank
    val visible = listOf(state.casesClosed > 0, state.trueVerdicts > 0, rank != null).count { it }
    if (visible < 2) {
        Text(
            text = stringResource(R.string.menu_empty),
            style = MaterialTheme.typography.bodyMedium,
            color = GoldDim,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth()
        )
        return
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (state.casesClosed > 0) {
            StatCard(
                label = stringResource(R.string.stat_cases_closed),
                value = state.casesClosed.toString(),
                accent = GoldLight,
                modifier = Modifier.weight(1f)
            )
        }
        if (state.trueVerdicts > 0) {
            StatCard(
                label = stringResource(R.string.stat_true_verdicts),
                value = state.trueVerdicts.toString(),
                accent = RubyBright,
                modifier = Modifier.weight(1f)
            )
        }
        if (rank != null) {
            StatCard(
                label = stringResource(R.string.stat_best_rank),
                value = rankText(rank),
                accent = rankAccent(rank),
                modifier = Modifier.weight(1f)
            )
        }
    }
}
