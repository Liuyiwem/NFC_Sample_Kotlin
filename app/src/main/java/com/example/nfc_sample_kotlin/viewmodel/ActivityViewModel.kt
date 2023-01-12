package com.example.nfc_sample_kotlin.viewmodel

import android.content.Intent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfc_sample_kotlin.logi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ActivityViewModel : ViewModel() {

    private val _newIntent = MutableSharedFlow<Intent>(replay = 0)
    val newIntent = _newIntent.asSharedFlow()

    fun setNewIntent(intent: Intent) {
        viewModelScope.launch {
            _newIntent.emit(intent)
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