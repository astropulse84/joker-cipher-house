package com.Kawwuxu6.Wodfer.presentation.evidence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kawwuxu6.Wodfer.R
import com.Kawwuxu6.Wodfer.core.di.ServiceLocator
import com.Kawwuxu6.Wodfer.core.theme.Ash
import com.Kawwuxu6.Wodfer.core.theme.GoldLine
import com.Kawwuxu6.Wodfer.core.theme.InkBlack
import com.Kawwuxu6.Wodfer.core.theme.InkMid
import com.Kawwuxu6.Wodfer.core.theme.Ivory
import com.Kawwuxu6.Wodfer.core.theme.IvoryDim
import com.Kawwuxu6.Wodfer.core.theme.RubyBright
import com.Kawwuxu6.Wodfer.core.theme.RubyDeep
import com.Kawwuxu6.Wodfer.core.theme.RubyMark
import com.Kawwuxu6.Wodfer.core.theme.VelvetCard
import com.Kawwuxu6.Wodfer.core.theme.VelvetDeep
import com.Kawwuxu6.Wodfer.core.theme.VelvetGlow
import com.Kawwuxu6.Wodfer.core.ui.ComposureMarks
import com.Kawwuxu6.Wodfer.core.ui.EmptyNote
import com.Kawwuxu6.Wodfer.core.ui.PrimaryButton
import com.Kawwuxu6.Wodfer.core.ui.ScreenBackground
import com.Kawwuxu6.Wodfer.core.ui.ScreenHeader
import com.Kawwuxu6.Wodfer.core.ui.SecondaryButton
import com.Kawwuxu6.Wodfer.core.ui.SectionTitle
import com.Kawwuxu6.Wodfer.domain.model.Evidence
import com.Kawwuxu6.Wodfer.domain.model.EvidenceState
import com.Kawwuxu6.Wodfer.domain.model.SuspectMark

@Composable
fun EvidenceWallScreen(
    onBack: () -> Unit,
    onMenu: () -> Unit,
    onFinish: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: EvidenceWallViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is WallEvent.Finish -> onFinish(event.score)
            }
        }
    }

    val accused = state.marks.count { it == SuspectMark.SUSPECT }
    val canAccuse = !state.readOnly && accused == 1 && state.selectedMotive >= 0
    val untouched = state.evidence.all { it.state == EvidenceState.SEALED }

    ScreenBackground(
        modifier = modifier,
        tones = listOf(InkBlack.copy(alpha = 0.93f), VelvetCard.copy(alpha = 0.95f)),
        imageAlpha = 0.28f,
        lampX = 0.5f,
        lampY = 0.62f,
        lampAlpha = 0.08f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.evidence_wall),
                onBack = onBack,
                backDescription = stringResource(R.string.menu),
                trailing = {
                    ComposureMarks(
                        total = state.composureTotal,
                        left = state.composureLeft,
                        description = stringResource(
                            R.string.sd_composure,
                            state.composureLeft,
                            state.composureTotal
                        )
                    )
                }
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    SectionTitle(text = stringResource(R.string.section_evidence))
                }
                if (untouched && state.readOnly) {
                    item {
                        EmptyNote(text = stringResource(R.string.wall_empty))
                    }
                }
                item {
                    EvidenceGrid(
                        evidence = state.evidence,
                        textScale = state.textScale
                    )
                }
                item {
                    SectionTitle(text = stringResource(R.string.section_relations))
                }
                item {
                    RelationBoard(
                        state = state,
                        onNode = viewModel::onNode
                    )
                }
                item {
                    SuspectStrip(state = state)
                }
                item {
                    SectionTitle(text = stringResource(R.string.section_motive))
                }
                item {
                    MotiveRow(
                        motives = state.motives,
                        selected = state.selectedMotive,
                        enabled = !state.readOnly,
                        onMotive = viewModel::onMotive
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                if (!state.readOnly) {
                    PrimaryButton(
                        text = stringResource(R.string.accuse),
                        onClick = viewModel::onAccuse,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = canAccuse,
                        leadingIcon = R.drawable.ic_seal,
                        tones = listOf(RubyMark, RubyBright, RubyMark),
                        contentColor = Ivory,
                        height = 60.dp,
                        stateHint = if (canAccuse) {
                            null
                        } else {
                            stringResource(R.string.sd_accuse_blocked)
                        }
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
}

@Composable
private fun EvidenceGrid(
    evidence: List<Evidence>,
    textScale: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        for (row in 0 until 2) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                for (column in 0 until 2) {
                    val index = row * 2 + column
                    val item = evidence.getOrNull(index)
                    if (item != null) {
                        EvidenceTile(
                            item = item,
                            textScale = textScale,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EvidenceTile(
    item: Evidence,
    textScale: Float,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(14.dp)
    val isSealed = item.state == EvidenceState.SEALED
    val verified = item.state == EvidenceState.VERIFIED
    val tint = when (item.state) {
        EvidenceState.VERIFIED -> GoldLine
        EvidenceState.UNVERIFIED -> Ash
        EvidenceState.SEALED -> Ash.copy(alpha = 0.5f)
    }
    val stateText = stringResource(
        when (item.state) {
            EvidenceState.VERIFIED -> R.string.sd_verified
            EvidenceState.UNVERIFIED -> R.string.sd_unverified
            EvidenceState.SEALED -> R.string.sd_sealed
        }
    )
    Column(
        modifier = modifier
            .height(118.dp)
            .clip(shape)
            .background(Brush.verticalGradient(listOf(VelvetCard, VelvetDeep)))
            .border(1.dp, tint.copy(alpha = 0.6f), shape)
            .semantics { stateDescription = stateText }
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(
                when (item.state) {
                    EvidenceState.VERIFIED -> R.drawable.ic_check
                    EvidenceState.UNVERIFIED -> R.drawable.ic_cross
                    EvidenceState.SEALED -> R.drawable.ic_lock
                }
            ),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = tint
        )
        Text(
            text = if (isSealed) stringResource(R.string.tile_sealed) else item.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = MaterialTheme.typography.bodyMedium.fontSize * textScale
            ),
            color = if (verified) Ivory else IvoryDim,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun RelationBoard(
    state: EvidenceWallUiState,
    onNode: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    BoxWidthHeight(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.1f)
            .clip(shape)
            .background(InkMid.copy(alpha = 0.55f))
            .border(1.dp, GoldLine.copy(alpha = 0.25f), shape)
    ) { boardWidth, boardHeight ->
        Canvas(modifier = Modifier.fillMaxSize()) {
            val pitch = 24.dp.toPx()
            var gridX = pitch
            while (gridX < size.width) {
                drawLine(
                    color = GoldLine.copy(alpha = 0.06f),
                    start = Offset(gridX, 0f),
                    end = Offset(gridX, size.height),
                    strokeWidth = 1f
                )
                gridX += pitch
            }
            var gridY = pitch
            while (gridY < size.height) {
                drawLine(
                    color = GoldLine.copy(alpha = 0.06f),
                    start = Offset(0f, gridY),
                    end = Offset(size.width, gridY),
                    strokeWidth = 1f
                )
                gridY += pitch
            }
            state.relations.forEachIndexed { index, relation ->
                val from = state.suspects.getOrNull(relation.from)
                val to = state.suspects.getOrNull(relation.to)
                if (from != null && to != null) {
                    val known = state.discovered.contains(index)
                    drawLine(
                        color = if (known) GoldLine else Ash.copy(alpha = 0.22f),
                        start = Offset(size.width * from.x, size.height * from.y),
                        end = Offset(size.width * to.x, size.height * to.y),
                        strokeWidth = 1.2.dp.toPx()
                    )
                }
            }
        }
        state.relations.forEachIndexed { index, relation ->
            val from = state.suspects.getOrNull(relation.from)
            val to = state.suspects.getOrNull(relation.to)
            if (from != null && to != null) {
                val known = state.discovered.contains(index)
                Box(
                    modifier = Modifier
                        .offset(
                            x = boardWidth * ((from.x + to.x) / 2f) - 30.dp,
                            y = boardHeight * ((from.y + to.y) / 2f) - 9.dp
                        )
                        .width(60.dp)
                ) {
                    Text(
                        text = relation.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (known) GoldLine else Ash,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(VelvetDeep.copy(alpha = 0.92f))
                            .padding(vertical = 2.dp)
                    )
                }
            }
        }
        state.suspects.forEachIndexed { index, suspect ->
            SuspectNode(
                name = suspect.name,
                mark = state.marks.getOrNull(index) ?: SuspectMark.NONE,
                enabled = !state.readOnly,
                onClick = { onNode(index) },
                modifier = Modifier.offset(
                    x = boardWidth * suspect.x - 38.dp,
                    y = boardHeight * suspect.y - 44.dp
                )
            )
        }
    }
}

@Composable
private fun BoxWidthHeight(
    modifier: Modifier = Modifier,
    content: @Composable androidx.compose.foundation.layout.BoxScope.(Dp, Dp) -> Unit
) {
    androidx.compose.foundation.layout.BoxWithConstraints(modifier = modifier) {
        content(maxWidth, maxHeight)
    }
}

@Composable
private fun SuspectNode(
    name: String,
    mark: SuspectMark,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ring = when (mark) {
        SuspectMark.SUSPECT -> RubyBright
        SuspectMark.CLEARED -> GoldLine
        SuspectMark.NONE -> GoldLine.copy(alpha = 0.4f)
    }
    val markText = stringResource(
        when (mark) {
            SuspectMark.SUSPECT -> R.string.sd_suspect_marked
            SuspectMark.CLEARED -> R.string.sd_suspect_cleared
            SuspectMark.NONE -> R.string.sd_suspect_none
        }
    )
    Column(
        modifier = modifier
            .width(76.dp)
            .semantics { stateDescription = markText }
            .then(if (enabled) Modifier.clickable { onClick() } else Modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(VelvetCard)
                .border(if (mark == SuspectMark.NONE) 1.dp else 2.dp, ring, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_2),
                contentDescription = stringResource(R.string.cd_suspect),
                modifier = Modifier.size(46.dp),
                contentScale = ContentScale.Fit
            )
            if (mark != SuspectMark.NONE) {
                Icon(
                    painter = painterResource(
                        if (mark == SuspectMark.SUSPECT) R.drawable.ic_seal else R.drawable.ic_check
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(16.dp),
                    tint = ring
                )
            }
        }
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = IvoryDim,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun SuspectStrip(
    state: EvidenceWallUiState,
    modifier: Modifier = Modifier
) {
    val suspect = state.suspects.getOrNull(state.focused) ?: return
    val shape = RoundedCornerShape(12.dp)
    val known = state.relations
        .filterIndexed { index, relation ->
            state.discovered.contains(index) &&
                (relation.from == state.focused || relation.to == state.focused)
        }
        .joinToString(SEPARATOR) { it.label }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VelvetCard.copy(alpha = 0.8f))
            .border(1.dp, GoldLine.copy(alpha = 0.25f), shape)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = suspect.name,
            style = MaterialTheme.typography.titleMedium,
            color = Ivory,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = suspect.role,
            style = MaterialTheme.typography.labelSmall,
            color = IvoryDim,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (known.isNotEmpty()) {
            Text(
                text = known,
                style = MaterialTheme.typography.labelSmall,
                color = GoldLine,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun MotiveRow(
    motives: List<String>,
    selected: Int,
    enabled: Boolean,
    onMotive: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        motives.forEachIndexed { index, motive ->
            val active = index == selected
            val shape = RoundedCornerShape(12.dp)
            val chosenText = stringResource(
                if (active) R.string.sd_selected else R.string.sd_not_selected
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(shape)
                    .background(if (active) VelvetGlow else VelvetCard)
                    .border(
                        width = if (active) 1.5.dp else 1.dp,
                        color = if (active) GoldLine else RubyDeep.copy(alpha = 0.5f),
                        shape = shape
                    )
                    .semantics { stateDescription = chosenText }
                    .then(if (enabled) Modifier.clickable { onMotive(index) } else Modifier)
                    .padding(horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = motive,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (active) Ivory else IvoryDim,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private const val SEPARATOR = " · "
