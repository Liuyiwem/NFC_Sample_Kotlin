package com.example.nfc_sample_kotlin.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.logi
import com.example.nfc_sample_kotlin.view.state.ScanDataState
import com.example.nfc_sample_kotlin.usecase.ScanDataUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ScanFragmentViewModel(private val scanDataUseCase: ScanDataUseCase) : ViewModel() {

    private val _uiState = MutableSharedFlow<ScanDataState<List<Message>>>(replay = 0)
    val uiState = _uiState.asSharedFlow()

    fun getNdefMessage(intent: Intent) {
        viewModelScope.launch {
            scanDataUseCase(intent)
                .onStart { _uiState.emit(ScanDataState.loading())}
                .catch { _uiState.emit(ScanDataState.failure(it)) }
                .collect { _uiState.emit(ScanDataState.success(it)) }
        }
    }

    init {
        logi("scanViewOnCreated: ")
    }

    override fun onCleared() {
        logi("onCleared: ")
        super.onCleared()
    }
}