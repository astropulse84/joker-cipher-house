package com.Kawwuxu6.Wodfer.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Kawwuxu6.Wodfer.data.session.CaseSessionHolder
import com.Kawwuxu6.Wodfer.domain.model.ChapterData
import com.Kawwuxu6.Wodfer.domain.repository.CaseRepository
import com.Kawwuxu6.Wodfer.domain.repository.SettingsRepository
import com.Kawwuxu6.Wodfer.domain.usecase.GenerateCaseUseCase
import com.Kawwuxu6.Wodfer.domain.usecase.ResolveAccusationUseCase
import com.Kawwuxu6.Wodfer.domain.usecase.ResolveStageUseCase
import com.Kawwuxu6.Wodfer.domain.usecase.ScoreCaseUseCase
import com.Kawwuxu6.Wodfer.presentation.app.AppViewModel
import com.Kawwuxu6.Wodfer.presentation.casefiles.CaseFilesViewModel
import com.Kawwuxu6.Wodfer.presentation.evidence.EvidenceWallViewModel
import com.Kawwuxu6.Wodfer.presentation.game.GameViewModel
import com.Kawwuxu6.Wodfer.presentation.gameover.GameOverViewModel
import com.Kawwuxu6.Wodfer.presentation.menu.MenuViewModel
import com.Kawwuxu6.Wodfer.presentation.settings.SettingsViewModel
import com.Kawwuxu6.Wodfer.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val caseRepository: CaseRepository,
    private val settingsRepository: SettingsRepository,
    private val session: CaseSessionHolder,
    private val chapters: List<ChapterData>,
    private val generateCase: GenerateCaseUseCase,
    private val resolveStage: ResolveStageUseCase,
    private val resolveAccusation: ResolveAccusationUseCase,
    private val scoreCase: ScoreCaseUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) ->
                AppViewModel()

            modelClass.isAssignableFrom(SplashViewModel::class.java) ->
                SplashViewModel()

            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(caseRepository, session)

            modelClass.isAssignableFrom(CaseFilesViewModel::class.java) ->
                CaseFilesViewModel(caseRepository, settingsRepository, session, chapters)

            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(
                    caseRepository,
                    settingsRepository,
                    session,
                    generateCase,
                    resolveStage,
                    scoreCase
                )

            modelClass.isAssignableFrom(EvidenceWallViewModel::class.java) ->
                EvidenceWallViewModel(
                    caseRepository,
                    settingsRepository,
                    session,
                    generateCase,
                    resolveAccusation,
                    scoreCase
                )

            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(caseRepository, session)

            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(settingsRepository, caseRepository)

            else -> throw IllegalArgumentException(UNKNOWN)
        }
        @Suppress("UNCHECKED_CAST")
        return created as T
    }

    private companion object {
        const val UNKNOWN = "Unsupported ViewModel requested from AppViewModelFactory"
    }
}
