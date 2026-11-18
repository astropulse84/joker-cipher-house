package com.Kawwuxu6.Wodfer.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kawwuxu6.Wodfer.core.config.GameConfig
import com.Kawwuxu6.Wodfer.data.session.CaseSessionHolder
import com.Kawwuxu6.Wodfer.domain.model.CaseResult
import com.Kawwuxu6.Wodfer.domain.repository.CaseRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class GameOverUiState(
    val result: CaseResult? = null,
    val nextAvailable: Boolean = false
)

sealed interface GameOverEvent {
    data object OpenCase : GameOverEvent
}

class GameOverViewModel(
    private val caseRepository: CaseRepository,
    private val session: CaseSessionHolder
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    private val _events = Channel<GameOverEvent>(Channel.BUFFERED)
    val events: Flow<GameOverEvent> = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            session.result.collect { value ->
                _state.value = GameOverUiState(
                    result = value,
                    nextAvailable = value != null &&
                        value.chapterIndex + 1 < GameConfig.CHAPTERS &&
                        value.chapterIndex + 1 < caseRepository.unlockedChapters()
                )
            }
        }
    }

    fun replay() {
        val result = _state.value.result ?: return
        session.open(result.chapterIndex)
        viewModelScope.launch { _events.send(GameOverEvent.OpenCase) }
    }

    fun nextCase() {
        val result = _state.value.result ?: return
        if (!_state.value.nextAvailable) return
        session.open(result.chapterIndex + 1)
        viewModelScope.launch { _events.send(GameOverEvent.OpenCase) }
    }
}
