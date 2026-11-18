package com.Kawwuxu6.Wodfer.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Kawwuxu6.Wodfer.core.theme.GoldLine
import com.Kawwuxu6.Wodfer.core.theme.Ivory
import com.Kawwuxu6.Wodfer.core.theme.RubyDeep
import com.Kawwuxu6.Wodfer.core.theme.VelvetCard
import com.Kawwuxu6.Wodfer.core.theme.VelvetDeep

@Composable
fun VelvetPanel(
    modifier: Modifier = Modifier,
    corners: Dp = 20.dp,
    innerPadding: Dp = 16.dp,
    brackets: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(corners)
    Box(
        modifier = modifier
            .shadow(elevation = 18.dp, shape = shape, spotColor = RubyDeep, ambientColor = RubyDeep)
            .clip(shape)
            .background(Brush.verticalGradient(listOf(VelvetCard, VelvetDeep)))
            .border(1.dp, GoldLine.copy(alpha = 0.4f), shape)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(5.dp)
                .border(1.dp, Ivory.copy(alpha = 0.07f), RoundedCornerShape(corners - 5.dp))
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(RubyDeep.copy(alpha = 0.10f), VelvetCard.copy(alpha = 0f)),
                        center = Offset(0f, 0f),
                        radius = 460f
                    )
                )
        )
        if (brackets) {
            CornerBrackets(modifier = Modifier.matchParentSize())
        }
        Box(modifier = Modifier.padding(innerPadding), content = content)
    }
}

@Composable
private fun CornerBrackets(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val arm = 18.dp.toPx()
        val inset = 11.dp.toPx()
        val stroke = 1.2.dp.toPx()
        val tint = GoldLine.copy(alpha = 0.65f)
        val right = size.width - inset
        val bottom = size.height - inset
        drawLine(tint, Offset(inset, inset), Offset(inset + arm, inset), stroke)
        drawLine(tint, Offset(inset, inset), Offset(inset, inset + arm), stroke)
        drawLine(tint, Offset(right, inset), Offset(right - arm, inset), stroke)
        drawLine(tint, Offset(right, inset), Offset(right, inset + arm), stroke)
        drawLine(tint, Offset(inset, bottom), Offset(inset + arm, bottom), stroke)
        drawLine(tint, Offset(inset, bottom), Offset(inset, bottom - arm), stroke)
        drawLine(tint, Offset(right, bottom), Offset(right - arm, bottom), stroke)
        drawLine(tint, Offset(right, bottom), Offset(right, bottom - arm), stroke)
    }
}
