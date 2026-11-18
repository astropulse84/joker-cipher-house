package com.Kawwuxu6.Wodfer.domain.repository

import com.Kawwuxu6.Wodfer.domain.model.Difficulty
import com.Kawwuxu6.Wodfer.domain.model.Readability

interface SettingsRepository {
    fun readability(): Readability
    fun setReadability(value: Readability)
    fun difficulty(): Difficulty
    fun setDifficulty(value: Difficulty)
    fun legendPinned(): Boolean
    fun setLegendPinned(value: Boolean)
}
