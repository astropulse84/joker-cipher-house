package com.Kawwuxu6.Wodfer.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object CaseFiles : Screen
    data object Game : Screen
    data object EvidenceWall : Screen
    data object Settings : Screen
    data class GameOver(val score: Int) : Screen
}
