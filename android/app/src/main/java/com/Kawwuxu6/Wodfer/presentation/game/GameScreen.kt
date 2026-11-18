package com.Kawwuxu6.Wodfer.presentation.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.Kawwuxu6.Wodfer.core.theme.InkMid
import com.Kawwuxu6.Wodfer.core.theme.Ivory
import com.Kawwuxu6.Wodfer.core.theme.IvoryDim
import com.Kawwuxu6.Wodfer.core.theme.RubyBright
import com.Kawwuxu6.Wodfer.core.theme.VelvetCard
import com.Kawwuxu6.Wodfer.core.theme.VelvetDeep
import com.Kawwuxu6.Wodfer.core.ui.ClueCard
import com.Kawwuxu6.Wodfer.core.ui.ClueState
import com.Kawwuxu6.Wodfer.core.ui.ComposureMarks
import com.Kawwuxu6.Wodfer.core.ui.PrimaryButton
import com.Kawwuxu6.Wodfer.core.ui.ScreenBackground
import com.Kawwuxu6.Wodfer.core.ui.SecondaryButton
import com.Kawwuxu6.Wodfer.core.ui.VelvetPanel
import com.Kawwuxu6.Wodfer.domain.model.StageKind
import com.Kawwuxu6.Wodfer.presentation.common.chapterNumeral
import com.Kawwuxu6.Wodfer.presentation.dialog.PauseDialog

@Composable
fun GameScreen(
    onOpenWall: () -> Unit,
    onFinish: (Int) -> Unit,
    onMenu: () -> Unit,
    onCaseFiles: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: GameViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is GameEvent.OpenWall -> onOpenWall()
                is GameEvent.Finish -> onFinish(event.score)
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.onLeave() }
    }

    ScreenBackground(
        modifier = modifier,
        tones = listOf(InkBlack.copy(alpha = 0.88f), VelvetDeep.copy(alpha = 0.92f)),
        imageAlpha = 0.34f,
        lampX = 0.18f,
        lampY = 0.24f,
        lampAlpha = 0.11f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(
                state = state,
                onHold = viewModel::onPause
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                VelvetPanel(modifier = Modifier.fillMaxSize()) {
                    StageContent(
                        state = state,
                        onClue = viewModel::onClue,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                StampLine(
                    visible = state.stampVisible,
                    correct = state.stampCorrect,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 6.dp)
                )
            }
            StatStrip(state = state)
            GameControls(
                state = state,
                onInspect = viewModel::onInspect,
                onAdvance = viewModel::onAdvance,
                onOpenWall = onOpenWall
            )
        }
    }

    if (state.paused) {
        PauseDialog(
            onResume = viewModel::onResume,
            onCaseFiles = {
                viewModel.onResume()
                onCaseFiles()
            },
            onMenu = {
                viewModel.onResume()
                onMenu()
            }
        )
    }
}

@Composable
private fun GameHeader(
    state: GameUiState,
    onHold: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(InkBlack.copy(alpha = 0.86f))
            .drawBehind {
                drawLine(
                    color = GoldLine.copy(alpha = 0.3f),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onHold() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = stringResource(R.string.cd_pause),
                modifier = Modifier.size(26.dp),
                contentScale = ContentScale.Fit
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 72.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.chapter_label, chapterNumeral(state.chapterIndex)),
                style = MaterialTheme.typography.labelSmall,
                color = GoldLine,
                maxLines = 1
            )
            Text(
                text = state.chapterTitle,
                style = MaterialTheme.typography.titleMedium,
                color = Ivory,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
        ComposureMarks(
            total = state.composureTotal,
            left = state.composureLeft,
            description = stringResource(
                R.string.sd_composure,
                state.composureLeft,
                state.composureTotal
            ),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 14.dp)
        )
    }
}

@Composable
private fun StageContent(
    state: GameUiState,
    onClue: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state.stageKind) {
        StageKind.WITNESS -> StageWitness(state, onClue, modifier)
        StageKind.CIPHER -> StageCipher(state, onClue, modifier)
        StageKind.PROPS -> StageProps(state, onClue, modifier)
        StageKind.INTERROGATION -> StageInterrogation(state, onClue, modifier)
    }
}

@Composable
private fun StageWitness(
    state: GameUiState,
    onClue: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        FactStrip(
            label = stringResource(R.string.house_record),
            body = state.houseRecord,
            textScale = state.textScale
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.witnesses.forEachIndexed { index, witness ->
                ClueCard(
                    body = witness.claim,
                    title = witness.name,
                    lead = witness.time,
                    state = clueStateFor(state, index),
                    textScale = state.textScale,
                    onClick = { onClue(index) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StageCipher(
    state: GameUiState,
    onClue: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.cipher_legend),
            style = MaterialTheme.typography.labelSmall,
            color = GoldLight,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (state.legendVisible) 1f else 0.12f)
                .border(1.dp, GoldLine.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp, vertical = 8.dp)
        )
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            val gap = 8.dp
            val cardWidth = (maxWidth - gap * 4) / 5
            val cardHeight = cardWidth * 1.42f
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(gap)
            ) {
                state.cards.forEachIndexed { index, card ->
                    Box(
                        modifier = Modifier
                            .width(cardWidth)
                            .height(cardHeight)
                            .rotate(if (index % 2 == 1) 4f else -3f),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sprite_1),
                            contentDescription = stringResource(R.string.cd_marked_card),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(InkMid.copy(alpha = 0.78f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = card.rank,
                                style = MaterialTheme.typography.titleMedium,
                                color = Ivory
                            )
                        }
                    }
                }
            }
        }
        Text(
            text = stringResource(R.string.word_options),
            style = MaterialTheme.typography.labelSmall,
            color = GoldDim,
            modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.cipherOptions.forEachIndexed { index, option ->
                ClueCard(
                    body = option,
                    state = clueStateFor(state, index),
                    textScale = state.textScale,
                    centred = true,
                    onClick = { onClue(index) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StageProps(
    state: GameUiState,
    onClue: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        FactStrip(
            label = stringResource(R.string.genuine_mark),
            body = state.genuineMark,
            textScale = state.textScale
        )
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            val gap = 10.dp
            val tile = (maxWidth - gap * 2) / 3
            Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                for (row in 0 until 2) {
                    Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                        for (column in 0 until 3) {
                            val index = row * 3 + column
                            val prop = state.props.getOrNull(index)
                            if (prop != null) {
                                PropTile(
                                    descriptor = prop.descriptor,
                                    tileWidth = tile,
                                    clueState = clueStateFor(state, index),
                                    textScale = state.textScale,
                                    onClick = { onClue(index) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StageInterrogation(
    state: GameUiState,
    onClue: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(width = 86.dp, height = 108.dp)
                    .clip(RoundedCornerShape(50))
                    .background(VelvetCard)
                    .border(1.dp, GoldLine.copy(alpha = 0.55f), RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_2),
                    contentDescription = stringResource(R.string.cd_suspect),
                    modifier = Modifier.size(width = 72.dp, height = 94.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = state.targetName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Ivory,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = state.targetRole,
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldDim,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Text(
            text = stringResource(R.string.question_label),
            style = MaterialTheme.typography.labelSmall,
            color = GoldLine,
            modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.questions.forEachIndexed { index, question ->
                ClueCard(
                    body = question.question,
                    state = clueStateFor(state, index),
                    textScale = state.textScale,
                    onClick = { onClue(index) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PropTile(
    descriptor: String,
    tileWidth: androidx.compose.ui.unit.Dp,
    clueState: ClueState,
    textScale: Float,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)
    val tint = when (clueState) {
        ClueState.CORRECT -> GoldLine
        ClueState.WRONG -> RubyBright
        ClueState.STRUCK -> Ash.copy(alpha = 0.4f)
        ClueState.IDLE -> GoldLine.copy(alpha = 0.3f)
    }
    Column(
        modifier = Modifier
            .width(tileWidth)
            .alpha(if (clueState == ClueState.STRUCK) 0.35f else 1f)
            .then(if (clueState == ClueState.STRUCK) Modifier else Modifier.clickable { onClick() }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(tileWidth)
                .clip(shape)
                .background(Brush.verticalGradient(listOf(VelvetCard, VelvetDeep)))
                .border(1.dp, tint, shape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_3),
                contentDescription = stringResource(R.string.cd_prop),
                modifier = Modifier.size(tileWidth * 0.72f),
                contentScale = ContentScale.Fit
            )
        }
        Text(
            text = descriptor,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = MaterialTheme.typography.labelSmall.fontSize * textScale
            ),
            color = IvoryDim,
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
private fun FactStrip(
    label: String,
    body: String,
    textScale: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(InkMid.copy(alpha = 0.55f))
            .border(1.dp, GoldLine.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = GoldLine
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = MaterialTheme.typography.bodyLarge.fontSize * textScale
            ),
            color = Ivory,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun StampLine(
    visible: Boolean,
    correct: Boolean,
    modifier: Modifier = Modifier
) {
    val fade by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(220)
    )
    Box(
        modifier = modifier
            .alpha(fade)
            .clip(RoundedCornerShape(8.dp))
            .background(InkBlack.copy(alpha = 0.86f))
            .border(
                2.dp,
                if (correct) GoldLine else RubyBright,
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = stringResource(
                if (correct) R.string.stamp_secured else R.string.stamp_lie
            ),
            style = MaterialTheme.typography.labelLarge,
            color = if (correct) GoldLight else RubyBright
        )
    }
}

@Composable
private fun StatStrip(
    state: GameUiState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(InkMid.copy(alpha = 0.9f))
            .semantics { liveRegion = LiveRegionMode.Polite },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = stringResource(R.string.stage_counter, state.stage),
            style = MaterialTheme.typography.labelSmall,
            color = GoldLight
        )
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .padding(vertical = 10.dp)
                .background(GoldLine.copy(alpha = 0.3f))
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = stringResource(R.string.cd_lens),
                modifier = Modifier.size(16.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = stringResource(R.string.fragments_counter, state.fragments),
                style = MaterialTheme.typography.labelSmall,
                color = GoldLight,
                modifier = Modifier.padding(start = 6.dp)
            )
        }
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .padding(vertical = 10.dp)
                .background(GoldLine.copy(alpha = 0.3f))
        )
        Text(
            text = stringResource(R.string.evidence_counter, state.evidenceVerified),
            style = MaterialTheme.typography.labelSmall,
            color = GoldLight
        )
    }
}

@Composable
private fun GameControls(
    state: GameUiState,
    onInspect: () -> Unit,
    onAdvance: () -> Unit,
    onOpenWall: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)
    val inspectEnabled = state.inspectAllowed &&
        state.fragments >= 2 &&
        state.phase == GamePhase.IDLE &&
        state.struck < 0
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VelvetDeep.copy(alpha = 0.97f))
            .border(1.dp, GoldLine.copy(alpha = 0.35f), shape)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            SecondaryButton(
                text = stringResource(R.string.inspect),
                onClick = onInspect,
                modifier = Modifier.weight(1f),
                enabled = inspectEnabled,
                leadingIcon = R.drawable.ic_lens,
                stateHint = if (inspectEnabled) null else stringResource(R.string.sd_inspect_blocked)
            )
            SecondaryButton(
                text = stringResource(R.string.evidence_wall),
                onClick = onOpenWall,
                modifier = Modifier.weight(1f),
                leadingIcon = R.drawable.ic_wall
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            if (state.phase == GamePhase.RESOLVED && state.composureLeft > 0) {
                PrimaryButton(
                    text = stringResource(
                        if (state.stage >= 4) R.string.open_evidence_wall else R.string.deal_next_clue
                    ),
                    onClick = onAdvance,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = R.drawable.ic_seal
                )
            } else {
                Text(
                    text = stringResource(R.string.control_hint),
                    style = MaterialTheme.typography.labelSmall,
                    color = IvoryDim,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(VelvetCard.copy(alpha = 0.7f))
                        .padding(horizontal = 12.dp, vertical = 20.dp)
                )
            }
        }
    }
}

private fun clueStateFor(state: GameUiState, index: Int): ClueState = when {
    index == state.struck -> ClueState.STRUCK
    state.phase != GamePhase.RESOLVED -> ClueState.IDLE
    index == state.chosen && index == state.correctIndex -> ClueState.CORRECT
    index == state.chosen -> ClueState.WRONG
    index == state.correctIndex -> ClueState.CORRECT
    else -> ClueState.IDLE
}
