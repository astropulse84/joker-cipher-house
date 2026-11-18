package com.Kawwuxu6.Wodfer.presentation.app

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kawwuxu6.Wodfer.core.di.ServiceLocator
import com.Kawwuxu6.Wodfer.core.navigation.Screen
import com.Kawwuxu6.Wodfer.presentation.casefiles.CaseFilesScreen
import com.Kawwuxu6.Wodfer.presentation.evidence.EvidenceWallScreen
import com.Kawwuxu6.Wodfer.presentation.game.GameScreen
import com.Kawwuxu6.Wodfer.presentation.gameover.GameOverScreen
import com.Kawwuxu6.Wodfer.presentation.menu.MenuScreen
import com.Kawwuxu6.Wodfer.presentation.settings.SettingsScreen
import com.Kawwuxu6.Wodfer.presentation.splash.SplashScreen

@Composable
fun AppRoot(modifier: Modifier = Modifier) {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.factory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BackHandler(enabled = screen != Screen.Splash) {
        if (!appViewModel.back()) {
            context.hostActivity()?.finish()
        }
    }

    val goBack: () -> Unit = {
        if (!appViewModel.back()) appViewModel.openMenu()
    }

    AnimatedContent(
        targetState = screen,
        modifier = modifier.fillMaxSize(),
        transitionSpec = {
            if (targetState == Screen.Game) {
                (
                    slideInHorizontally(
                        animationSpec = tween(380, easing = FastOutSlowInEasing),
                        initialOffsetX = { full -> full / 3 }
                    ) + fadeIn(animationSpec = tween(320))
                    ) togetherWith fadeOut(animationSpec = tween(240))
            } else {
                fadeIn(animationSpec = tween(420)) togetherWith
                    fadeOut(animationSpec = tween(300))
            }
        },
        label = ROOT_LABEL
    ) { target ->
        when (target) {
            is Screen.Splash -> SplashScreen(
                onFinished = appViewModel::finishSplash
            )

            is Screen.Menu -> MenuScreen(
                onPlay = { appViewModel.navigate(Screen.Game) },
                onCaseFiles = { appViewModel.navigate(Screen.CaseFiles) },
                onEvidenceWall = { appViewModel.navigate(Screen.EvidenceWall) },
                onSettings = { appViewModel.navigate(Screen.Settings) }
            )

            is Screen.CaseFiles -> CaseFilesScreen(
                onBack = goBack,
                onOpenCase = { appViewModel.navigate(Screen.Game) }
            )

            is Screen.Game -> GameScreen(
                onOpenWall = { appViewModel.navigate(Screen.EvidenceWall) },
                onFinish = { score -> appViewModel.openResult(score) },
                onMenu = appViewModel::openMenu,
                onCaseFiles = { appViewModel.navigate(Screen.CaseFiles) }
            )

            is Screen.EvidenceWall -> EvidenceWallScreen(
                onBack = goBack,
                onMenu = appViewModel::openMenu,
                onFinish = { score -> appViewModel.openResult(score) }
            )

            is Screen.GameOver -> GameOverScreen(
                onPlayAgain = appViewModel::openCase,
                onMenu = appViewModel::openMenu
            )

            is Screen.Settings -> SettingsScreen(
                onBack = goBack
            )
        }
    }
}

private tailrec fun Context.hostActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.hostActivity()
    else -> null
}

private const val ROOT_LABEL = "root"
