package com.Kawwuxu6.Wodfer.data.session

import com.Kawwuxu6.Wodfer.domain.model.CaseProgress
import com.Kawwuxu6.Wodfer.domain.model.CaseRequest
import com.Kawwuxu6.Wodfer.domain.model.CaseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CaseSessionHolder {

    private val _request = MutableStateFlow<CaseRequest?>(null)
    val request: StateFlow<CaseRequest?> = _request.asStateFlow()

    private val _progress = MutableStateFlow<CaseProgress?>(null)
    val progress: StateFlow<CaseProgress?> = _progress.asStateFlow()

    private val _result = MutableStateFlow<CaseResult?>(null)
    val result: StateFlow<CaseResult?> = _result.asStateFlow()

    private var attempts = 0

    fun open(chapterIndex: Int) {
        attempts += 1
        _request.value = CaseRequest(chapterIndex, attempts)
    }

    fun publishProgress(value: CaseProgress) {
        _progress.value = value
    }

    fun publishResult(value: CaseResult) {
        _result.value = value
    }

    fun currentChapter(): Int = _request.value?.chapterIndex ?: 0
}
