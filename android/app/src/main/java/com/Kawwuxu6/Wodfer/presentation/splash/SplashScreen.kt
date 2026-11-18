package com.Kawwuxu6.Wodfer.presentation.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kawwuxu6.Wodfer.R
import com.Kawwuxu6.Wodfer.core.di.ServiceLocator
import com.Kawwuxu6.Wodfer.core.theme.GoldDim
import com.Kawwuxu6.Wodfer.core.theme.GoldLine
import com.Kawwuxu6.Wodfer.core.theme.InkDeep
import com.Kawwuxu6.Wodfer.core.theme.Ivory
import com.Kawwuxu6.Wodfer.core.theme.IvoryDim
import com.Kawwuxu6.Wodfer.core.theme.RubyMark
import com.Kawwuxu6.Wodfer.core.ui.ScreenBackground

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: SplashViewModel = viewModel(factory = ServiceLocator.factory)
    val finished by viewModel.finished.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) onFinished()
    }

    SplashContent(modifier = modifier)
}

@Composable
private fun SplashContent(modifier: Modifier = Modifier) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        appeared = true
    }
    val entry by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.62f, stiffness = 180f)
    )
    val transition = rememberInfiniteTransition()
    val spin by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    ScreenBackground(
        modifier = modifier,
        tones = listOf(
            InkDeep.copy(alpha = 0.97f),
            Color(0xFF140A10).copy(alpha = 0.95f),
            Color(0xFF2A0C16).copy(alpha = 0.92f)
        ),
        imageAlpha = 0.22f,
        lampX = 0.5f,
        lampY = 0.38f,
        lampAlpha = 0.10f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(232.dp)
                    .graphicsLayer {
                        val scale = 0.86f + 0.14f * entry
                        scaleX = scale
                        scaleY = scale
                        alpha = entry
                    },
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { rotationZ = spin }
                ) {
                    drawArc(
                        color = GoldLine.copy(alpha = 0.34f),
                        startAngle = 0f,
                        sweepAngle = 300f,
                        useCenter = false,
                        style = Stroke(width = 1.2.dp.toPx())
                    )
                }
                Box(
                    modifier = Modifier
                        .size(196.dp)
                        .border(1.5.dp, GoldLine.copy(alpha = 0.8f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val arm = 22.dp.toPx()
                        val inset = 8.dp.toPx()
                        val stroke = 1.6.dp.toPx()
                        val right = size.width - inset
                        val bottom = size.height - inset
                        drawLine(GoldLine, Offset(inset, inset), Offset(inset + arm, inset), stroke)
                        drawLine(GoldLine, Offset(inset, inset), Offset(inset, inset + arm), stroke)
                        drawLine(GoldLine, Offset(right, inset), Offset(right - arm, inset), stroke)
                        drawLine(GoldLine, Offset(right, inset), Offset(right, inset + arm), stroke)
                        drawLine(GoldLine, Offset(inset, bottom), Offset(inset + arm, bottom), stroke)
                        drawLine(GoldLine, Offset(inset, bottom), Offset(inset, bottom - arm), stroke)
                        drawLine(GoldLine, Offset(right, bottom), Offset(right - arm, bottom), stroke)
                        drawLine(GoldLine, Offset(right, bottom), Offset(right, bottom - arm), stroke)
                    }
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.cd_logo),
                        modifier = Modifier.size(150.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Text(
                text = stringResource(R.string.title_app),
                style = MaterialTheme.typography.displaySmall,
                color = Ivory,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 36.dp)
                    .alpha(entry)
            )
            Box(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .width(120.dp)
                    .height(1.dp)
                    .background(GoldLine.copy(alpha = 0.55f))
            )
            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = IvoryDim,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp)
            )
            Text(
                text = stringResource(R.string.brand),
                style = MaterialTheme.typography.labelSmall,
                color = GoldDim,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp)
            )
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .width(190.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = GoldLine,
                trackColor = Color(0xFF2A2118)
            )
            WaxDots(modifier = Modifier.padding(top = 20.dp))
        }
    }
}

@Composable
private fun WaxDots(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (index in 0 until 3) {
            val pulse by transition.animateFloat(
                initialValue = 0.25f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(900, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(index * 240)
                )
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(RubyMark.copy(alpha = pulse))
            )
        }
    }
}
