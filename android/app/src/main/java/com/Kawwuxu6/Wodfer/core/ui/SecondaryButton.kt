package com.Kawwuxu6.Wodfer.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Kawwuxu6.Wodfer.core.theme.Ash
import com.Kawwuxu6.Wodfer.core.theme.GoldLine
import com.Kawwuxu6.Wodfer.core.theme.Ivory
import com.Kawwuxu6.Wodfer.core.theme.VelvetCard

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: Int? = null,
    iconDescription: String? = null,
    accent: Color = GoldLine,
    height: Dp = 50.dp,
    stateHint: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 420f)
    )
    val hint = stateHint
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(height.coerceAtLeast(48.dp))
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(
                if (hint != null) Modifier.semantics { stateDescription = hint } else Modifier
            ),
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        interactionSource = interactionSource,
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) accent.copy(alpha = 0.45f) else Ash.copy(alpha = 0.45f)
        ),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = VelvetCard.copy(alpha = 0.85f),
            contentColor = Ivory,
            disabledContainerColor = VelvetCard.copy(alpha = 0.55f),
            disabledContentColor = Ivory.copy(alpha = 0.38f)
        )
    ) {
        if (leadingIcon != null) {
            Icon(
                painter = painterResource(leadingIcon),
                contentDescription = iconDescription,
                modifier = Modifier.size(20.dp),
                tint = if (enabled) accent else Ash
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(fontSize = MaterialTheme.typography.labelLarge.fontSize * 0.92f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
