package com.Kawwuxu6.Wodfer.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.Kawwuxu6.Wodfer.core.theme.Ash
import com.Kawwuxu6.Wodfer.core.theme.GoldLine
import com.Kawwuxu6.Wodfer.core.theme.Ivory
import com.Kawwuxu6.Wodfer.core.theme.IvoryDim
import com.Kawwuxu6.Wodfer.core.theme.RubyBright
import com.Kawwuxu6.Wodfer.core.theme.VelvetCard
import com.Kawwuxu6.Wodfer.core.theme.VelvetDeep

enum class ClueState { IDLE, CORRECT, WRONG, STRUCK }

@Composable
fun ClueCard(
    body: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    lead: String? = null,
    state: ClueState = ClueState.IDLE,
    textScale: Float = 1f,
    centred: Boolean = false,
    stateHint: String? = null,
    onClick: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(14.dp)
    val borderTint by animateColorAsState(
        targetValue = when (state) {
            ClueState.CORRECT -> GoldLine
            ClueState.WRONG -> RubyBright
            ClueState.STRUCK -> Ash.copy(alpha = 0.4f)
            ClueState.IDLE -> GoldLine.copy(alpha = 0.32f)
        },
        animationSpec = tween(220)
    )
    val scale by animateFloatAsState(
        targetValue = if (state == ClueState.CORRECT || state == ClueState.WRONG) 1.04f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 320f)
    )
    val faded = state == ClueState.STRUCK
    val hint = stateHint
    val action = onClick
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = if (faded) 0.35f else 1f
            }
            .clip(shape)
            .background(Brush.verticalGradient(listOf(VelvetCard, VelvetDeep)))
            .border(if (state == ClueState.IDLE) 1.dp else 1.6.dp, borderTint, shape)
            .then(
                if (action != null && !faded) Modifier.clickable { action() } else Modifier
            )
            .then(
                if (hint != null) Modifier.semantics { stateDescription = hint } else Modifier
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (lead != null) {
                Box(
                    modifier = Modifier
                        .widthIn(min = 48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(GoldLine.copy(alpha = 0.12f))
                        .border(1.dp, GoldLine.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = lead,
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldLine,
                        maxLines = 1
                    )
                }
                Box(modifier = Modifier.width(12.dp))
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = if (centred) Alignment.CenterHorizontally else Alignment.Start
            ) {
                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldLine,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize * textScale,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * textScale
                    ),
                    color = if (faded) IvoryDim else Ivory,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = if (centred) TextAlign.Center else TextAlign.Start,
                    modifier = Modifier.padding(top = if (title != null) 4.dp else 0.dp)
                )
            }
        }
    }
}
