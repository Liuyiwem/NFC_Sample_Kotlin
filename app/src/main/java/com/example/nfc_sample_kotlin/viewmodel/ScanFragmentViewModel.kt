package com.example.nfc_sample_kotlin.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.logi
import com.example.nfc_sample_kotlin.view.state.ScanDataState
import com.example.nfc_sample_kotlin.usecase.ScanDataUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ScanFragmentViewModel(private val scanDataUseCase: ScanDataUseCase) : ViewModel() {

    private val _ndefMessage = MutableLiveData<ScanDataState<List<Message>>>()
    val ndefMessage: LiveData<ScanDataState<List<Message>>>
        get() = _ndefMessage

    fun getNdefMessage(intent: Intent) {
        viewModelScope.launch {
            scanDataUseCase(intent)
                .onStart { _ndefMessage.value = ScanDataState.loading() }
                .catch { _ndefMessage.value = ScanDataState.failure(it) }
                .collect { _ndefMessage.value = ScanDataState.success(it) }
        }
    }

    init {
        logi("scanViewOnCreated: ")
    }

    override fun onCleared() {
        super.onCleared()
        logi("onCleared: ")
    }
}