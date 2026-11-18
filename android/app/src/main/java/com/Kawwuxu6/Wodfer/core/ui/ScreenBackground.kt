package com.Kawwuxu6.Wodfer.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.Kawwuxu6.Wodfer.R
import com.Kawwuxu6.Wodfer.core.theme.GoldLine
import com.Kawwuxu6.Wodfer.core.theme.InkBlack

@Composable
fun ScreenBackground(
    tones: List<Color>,
    modifier: Modifier = Modifier,
    imageAlpha: Float = 0.42f,
    lampX: Float = 0.5f,
    lampY: Float = 0.3f,
    lampAlpha: Float = 0.10f,
    lampTint: Color = GoldLine,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(InkBlack)
    ) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = imageAlpha
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Brush.verticalGradient(tones))
                .drawBehind {
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                lampTint.copy(alpha = lampAlpha),
                                Color.Transparent
                            ),
                            center = Offset(size.width * lampX, size.height * lampY),
                            radius = size.minDimension * 0.9f
                        )
                    )
                }
        )
        content()
    }
}
