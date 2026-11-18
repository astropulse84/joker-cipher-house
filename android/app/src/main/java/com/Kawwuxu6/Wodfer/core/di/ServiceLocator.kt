package com.Kawwuxu6.Wodfer.core.di

import android.content.Context
import com.Kawwuxu6.Wodfer.data.local.PreferencesStore
import com.Kawwuxu6.Wodfer.data.repository.CaseRepositoryImpl
import com.Kawwuxu6.Wodfer.data.repository.SettingsRepositoryImpl
import com.Kawwuxu6.Wodfer.data.sample.SampleData
import com.Kawwuxu6.Wodfer.data.session.CaseSessionHolder
import com.Kawwuxu6.Wodfer.domain.usecase.GenerateCaseUseCase
import com.Kawwuxu6.Wodfer.domain.usecase.ResolveAccusationUseCase
import com.Kawwuxu6.Wodfer.domain.usecase.ResolveStageUseCase
import com.Kawwuxu6.Wodfer.domain.usecase.ScoreCaseUseCase

object ServiceLocator {

    private var viewModelFactory: AppViewModelFactory? = null

    fun init(context: Context) {
        if (viewModelFactory != null) return
        val store = PreferencesStore(context.applicationContext)
        val chapters = SampleData.chapters
        viewModelFactory = AppViewModelFactory(
            caseRepository = CaseRepositoryImpl(store, chapters),
            settingsRepository = SettingsRepositoryImpl(store),
            session = CaseSessionHolder(),
            chapters = chapters,
            generateCase = GenerateCaseUseCase(chapters),
            resolveStage = ResolveStageUseCase(),
            resolveAccusation = ResolveAccusationUseCase(),
            scoreCase = ScoreCaseUseCase()
        )
    }

    val factory: AppViewModelFactory
        get() = viewModelFactory ?: throw IllegalStateException(NOT_READY)

    private const val NOT_READY = "ServiceLocator.init must run before any ViewModel is created"
}
