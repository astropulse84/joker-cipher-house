package com.Kawwuxu6.Wodfer.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Kawwuxu6.Wodfer.R
import com.Kawwuxu6.Wodfer.core.theme.GoldLine
import com.Kawwuxu6.Wodfer.core.theme.RubyBright

@Composable
fun SealBadge(
    accent: Color,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    ringSweep: Float = 1f,
    cracked: Boolean = false,
    description: String? = null
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val outer = this.size.minDimension / 2f
            val stroke = outer * 0.055f
            drawCircle(
                color = GoldLine.copy(alpha = 0.75f),
                radius = outer - stroke,
                style = Stroke(width = stroke)
            )
            drawCircle(
                color = GoldLine.copy(alpha = 0.35f),
                radius = outer * 0.78f,
                style = Stroke(width = stroke * 0.8f)
            )
            if (ringSweep > 0f) {
                drawArc(
                    color = accent,
                    startAngle = -90f,
                    sweepAngle = 360f * ringSweep,
                    useCenter = false,
                    topLeft = Offset(stroke, stroke),
                    size = androidx.compose.ui.geometry.Size(
                        this.size.width - stroke * 2f,
                        this.size.height - stroke * 2f
                    ),
                    style = Stroke(width = stroke * 1.2f)
                )
            }
            if (cracked) {
                drawLine(
                    color = RubyBright,
                    start = Offset(this.size.width * 0.24f, this.size.height * 0.78f),
                    end = Offset(this.size.width * 0.76f, this.size.height * 0.22f),
                    strokeWidth = stroke * 1.6f
                )
            }
        }
        Icon(
            painter = painterResource(R.drawable.ic_seal),
            contentDescription = description,
            modifier = Modifier.size(size * 0.46f),
            tint = accent
        )
    }
}
