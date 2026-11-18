package com.Kawwuxu6.Wodfer.presentation.app

import androidx.lifecycle.ViewModel
import com.Kawwuxu6.Wodfer.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val backStack = mutableListOf<Screen>()

    fun navigate(target: Screen) {
        val current = _screen.value
        if (current != Screen.Splash) backStack.add(current)
        _screen.value = target
    }

    fun finishSplash() {
        backStack.clear()
        _screen.value = Screen.Menu
    }

    fun openResult(score: Int) {
        backStack.clear()
        backStack.add(Screen.Menu)
        _screen.value = Screen.GameOver(score)
    }

    fun openCase() {
        backStack.clear()
        backStack.add(Screen.Menu)
        _screen.value = Screen.Game
    }

    fun openMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
    }

    fun back(): Boolean {
        val previous = backStack.removeLastOrNull() ?: return false
        _screen.value = previous
        return true
    }
}
