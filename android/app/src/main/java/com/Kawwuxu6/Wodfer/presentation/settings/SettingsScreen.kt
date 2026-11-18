package com.Kawwuxu6.Wodfer.presentation.settings

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kawwuxu6.Wodfer.R
import com.Kawwuxu6.Wodfer.core.di.ServiceLocator
import com.Kawwuxu6.Wodfer.core.theme.Ash
import com.Kawwuxu6.Wodfer.core.theme.GoldLine
import com.Kawwuxu6.Wodfer.core.theme.InkBlack
import com.Kawwuxu6.Wodfer.core.theme.Ivory
import com.Kawwuxu6.Wodfer.core.theme.IvoryDim
import com.Kawwuxu6.Wodfer.core.theme.OnGold
import com.Kawwuxu6.Wodfer.core.theme.RubyBright
import com.Kawwuxu6.Wodfer.core.theme.VelvetCard
import com.Kawwuxu6.Wodfer.core.theme.VelvetDeep
import com.Kawwuxu6.Wodfer.core.theme.VelvetGlow
import com.Kawwuxu6.Wodfer.core.ui.ScreenBackground
import com.Kawwuxu6.Wodfer.core.ui.ScreenHeader
import com.Kawwuxu6.Wodfer.core.ui.SecondaryButton
import com.Kawwuxu6.Wodfer.domain.model.Difficulty
import com.Kawwuxu6.Wodfer.domain.model.Readability
import com.Kawwuxu6.Wodfer.presentation.dialog.ConfirmClearDialog

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: SettingsViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.reload()
    }

    ScreenBackground(
        modifier = modifier,
        tones = listOf(InkBlack.copy(alpha = 0.9f), VelvetDeep.copy(alpha = 0.94f)),
        imageAlpha = 0.26f,
        lampX = 0.2f,
        lampY = 0.8f,
        lampAlpha = 0.08f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.settings),
                onBack = onBack,
                backDescription = stringResource(R.string.menu)
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    SettingRow(
                        caption = stringResource(R.string.settings_readability_title),
                        body = stringResource(R.string.settings_readability_body)
                    ) {
                        SegmentedPair(
                            firstLabel = stringResource(R.string.standard),
                            secondLabel = stringResource(R.string.large),
                            firstActive = state.readability == Readability.STANDARD,
                            onFirst = { viewModel.setReadability(Readability.STANDARD) },
                            onSecond = { viewModel.setReadability(Readability.LARGE) }
                        )
                    }
                }
                item {
                    SettingRow(
                        caption = stringResource(R.string.settings_difficulty_title),
                        body = stringResource(R.string.settings_difficulty_body)
                    ) {
                        SegmentedPair(
                            firstLabel = stringResource(R.string.investigator),
                            secondLabel = stringResource(R.string.inspector),
                            firstActive = state.difficulty == Difficulty.INVESTIGATOR,
                            onFirst = { viewModel.setDifficulty(Difficulty.INVESTIGATOR) },
                            onSecond = { viewModel.setDifficulty(Difficulty.INSPECTOR) }
                        )
                    }
                }
                item {
                    SettingRow(
                        caption = stringResource(R.string.settings_legend_title),
                        body = stringResource(R.string.settings_legend_body)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Switch(
                                checked = state.legendPinned,
                                onCheckedChange = viewModel::setLegend,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = OnGold,
                                    checkedTrackColor = GoldLine,
                                    uncheckedThumbColor = Ash,
                                    uncheckedTrackColor = VelvetGlow
                                )
                            )
                        }
                    }
                }
                item {
                    SettingRow(
                        caption = stringResource(R.string.settings_clear_title),
                        body = stringResource(R.string.settings_clear_body)
                    ) {
                        OutlinedButton(
                            onClick = viewModel::askClear,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.clear_history),
                                style = MaterialTheme.typography.labelLarge,
                                color = RubyBright
                            )
                        }
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

    if (state.confirmClear) {
        ConfirmClearDialog(
            onConfirm = viewModel::confirmClear,
            onCancel = viewModel::cancelClear
        )
    }
}

@Composable
private fun SettingRow(
    caption: String,
    body: String,
    modifier: Modifier = Modifier,
    control: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Brush.verticalGradient(listOf(VelvetCard, VelvetDeep)))
            .border(1.dp, GoldLine.copy(alpha = 0.3f), shape)
            .padding(16.dp)
    ) {
        Text(
            text = caption,
            style = MaterialTheme.typography.labelSmall,
            color = GoldLine
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = IvoryDim,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )
        control()
    }
}

@Composable
private fun SegmentedPair(
    firstLabel: String,
    secondLabel: String,
    firstActive: Boolean,
    onFirst: () -> Unit,
    onSecond: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Segment(
            label = firstLabel,
            active = firstActive,
            onClick = onFirst,
            modifier = Modifier.weight(1f)
        )
        Segment(
            label = secondLabel,
            active = !firstActive,
            onClick = onSecond,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun Segment(
    label: String,
    active: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    val stateText = stringResource(
        if (active) R.string.sd_selected else R.string.sd_not_selected
    )
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .background(if (active) VelvetGlow else VelvetCard)
            .border(
                width = if (active) 1.5.dp else 1.dp,
                color = if (active) GoldLine else Ash.copy(alpha = 0.6f),
                shape = shape
            )
            .semantics { stateDescription = stateText }
            .clickable { onClick() }
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (active) Ivory else IvoryDim,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
