package com.Kawwuxu6.Wodfer.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Kawwuxu6.Wodfer.R
import com.Kawwuxu6.Wodfer.core.theme.GoldDim
import com.Kawwuxu6.Wodfer.core.theme.RubyBright

@Composable
fun ComposureMarks(
    total: Int,
    left: Int,
    description: String,
    modifier: Modifier = Modifier,
    markSize: Dp = 18.dp
) {
    Row(
        modifier = modifier.semantics {
            stateDescription = description
            liveRegion = LiveRegionMode.Polite
        },
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (index in 0 until total) {
            val intact = index < left
            val markAlpha by animateFloatAsState(
                targetValue = if (intact) 1f else 0.42f,
                animationSpec = tween(260)
            )
            Icon(
                painter = painterResource(
                    if (intact) R.drawable.ic_seal else R.drawable.ic_seal_empty
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(markSize)
                    .alpha(markAlpha),
                tint = if (intact) RubyBright else GoldDim
            )
        }
    }
}
