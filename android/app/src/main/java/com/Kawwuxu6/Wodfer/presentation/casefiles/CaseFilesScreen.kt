package com.Kawwuxu6.Wodfer.presentation.casefiles

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kawwuxu6.Wodfer.R
import com.Kawwuxu6.Wodfer.core.di.ServiceLocator
import com.Kawwuxu6.Wodfer.core.theme.Ash
import com.Kawwuxu6.Wodfer.core.theme.GoldLight
import com.Kawwuxu6.Wodfer.core.theme.GoldLine
import com.Kawwuxu6.Wodfer.core.theme.InkBlack
import com.Kawwuxu6.Wodfer.core.theme.Ivory
import com.Kawwuxu6.Wodfer.core.theme.IvoryDim
import com.Kawwuxu6.Wodfer.core.theme.VelvetCard
import com.Kawwuxu6.Wodfer.core.theme.VelvetDeep
import com.Kawwuxu6.Wodfer.core.ui.EmptyNote
import com.Kawwuxu6.Wodfer.core.ui.ScreenBackground
import com.Kawwuxu6.Wodfer.core.ui.ScreenHeader
import com.Kawwuxu6.Wodfer.core.ui.SealBadge
import com.Kawwuxu6.Wodfer.core.ui.SecondaryButton
import com.Kawwuxu6.Wodfer.core.ui.SectionTitle
import com.Kawwuxu6.Wodfer.domain.model.CaseRecord
import com.Kawwuxu6.Wodfer.domain.model.ChapterSummary
import com.Kawwuxu6.Wodfer.presentation.common.chapterNumeral
import com.Kawwuxu6.Wodfer.presentation.common.rankAccent
import com.Kawwuxu6.Wodfer.presentation.common.rankText
import com.Kawwuxu6.Wodfer.presentation.common.verdictAccent
import com.Kawwuxu6.Wodfer.presentation.common.verdictWord

@Composable
fun CaseFilesScreen(
    onBack: () -> Unit,
    onOpenCase: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: CaseFilesViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    ScreenBackground(
        modifier = modifier,
        tones = listOf(InkBlack.copy(alpha = 0.9f), VelvetDeep.copy(alpha = 0.95f)),
        imageAlpha = 0.3f,
        lampX = 0.82f,
        lampY = 0.12f,
        lampAlpha = 0.09f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.case_files),
                onBack = onBack,
                backDescription = stringResource(R.string.menu)
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    SectionTitle(text = stringResource(R.string.section_chapter_dossiers))
                }
                items(state.chapters.size) { position ->
                    val chapter = state.chapters[position]
                    DossierRow(
                        chapter = chapter,
                        textScale = state.textScale,
                        onOpen = {
                            viewModel.openChapter(chapter.index)
                            onOpenCase()
                        }
                    )
                }
                item {
                    SectionTitle(
                        text = stringResource(R.string.section_case_history),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                if (state.history.isEmpty()) {
                    item {
                        EmptyNote(text = stringResource(R.string.history_empty))
                    }
                } else {
                    items(state.history.size) { position ->
                        HistoryRow(
                            record = state.history[position],
                            textScale = state.textScale
                        )
                    }
                }
            }
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                leadingIcon = R.drawable.icon_home
            )
        }
    }
}

@Composable
private fun DossierRow(
    chapter: ChapterSummary,
    textScale: Float,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    val locked = !chapter.unlocked
    val rank = chapter.bestRank
    val lockedText = stringResource(R.string.sd_locked)
    val openText = stringResource(R.string.sd_unlocked)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(92.dp)
            .alpha(if (locked) 0.45f else 1f)
            .clip(shape)
            .background(Brush.verticalGradient(listOf(VelvetCard, VelvetDeep)))
            .border(1.dp, GoldLine.copy(alpha = 0.35f), shape)
            .then(if (locked) Modifier else Modifier.clickable { onOpen() })
            .semantics {
                stateDescription = if (locked) lockedText else openText
            }
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = stringResource(R.string.cd_dossier),
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.chapter_label, chapterNumeral(chapter.index)),
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldLine
                )
                Text(
                    text = chapter.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Ivory,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = chapter.premise,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize * textScale
                    ),
                    color = IvoryDim,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (locked) {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = Ash
                )
            } else if (rank != null) {
                SealBadge(accent = rankAccent(rank), size = 40.dp)
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_lens),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = GoldLine.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun HistoryRow(
    record: CaseRecord,
    textScale: Float,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(shape)
            .background(VelvetCard.copy(alpha = 0.85f))
            .border(1.dp, GoldLine.copy(alpha = 0.22f), shape)
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.chapterTitle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize * textScale
                    ),
                    color = Ivory,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = verdictWord(record.verdict),
                    style = MaterialTheme.typography.labelSmall,
                    color = verdictAccent(record.verdict)
                )
            }
            Box(modifier = Modifier.width(10.dp))
            Text(
                text = rankText(record.rank),
                style = MaterialTheme.typography.labelSmall,
                color = rankAccent(record.rank)
            )
            Box(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.score_value, record.score),
                style = MaterialTheme.typography.titleMedium,
                color = GoldLight
            )
        }
    }
}
