package com.Kawwuxu6.Wodfer.data.repository

import com.Kawwuxu6.Wodfer.data.local.PreferencesStore
import com.Kawwuxu6.Wodfer.domain.model.Difficulty
import com.Kawwuxu6.Wodfer.domain.model.Readability
import com.Kawwuxu6.Wodfer.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val store: PreferencesStore) : SettingsRepository {

    override fun readability(): Readability =
        Readability.entries.getOrNull(store.int(KEY_READABILITY, 0)) ?: Readability.STANDARD

    override fun setReadability(value: Readability) {
        store.putInt(KEY_READABILITY, value.ordinal)
    }

    override fun difficulty(): Difficulty =
        Difficulty.entries.getOrNull(store.int(KEY_DIFFICULTY, 0)) ?: Difficulty.INVESTIGATOR

    override fun setDifficulty(value: Difficulty) {
        store.putInt(KEY_DIFFICULTY, value.ordinal)
    }

    override fun legendPinned(): Boolean = store.bool(KEY_LEGEND, true)

    override fun setLegendPinned(value: Boolean) {
        store.putBool(KEY_LEGEND, value)
    }

    private companion object {
        const val KEY_READABILITY = "pref_readability"
        const val KEY_DIFFICULTY = "pref_difficulty"
        const val KEY_LEGEND = "pref_legend"
    }
}
