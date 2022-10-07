package com.example.nfc_sample_kotlin.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.repository.ScanDataRepository
import com.example.nfc_sample_kotlin.logi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanFragmentViewModel(private val scanDataRepository: ScanDataRepository) : ViewModel(){

    private val _listNdefPayload = MutableLiveData<List<Message>>()
    val listNdefPayload : LiveData<List<Message>> get() = _listNdefPayload

    fun parseNdefMessage(intent: Intent){
        viewModelScope.launch(Dispatchers.Default){
            val response = scanDataRepository.parseNdefMessage(intent)
            withContext(Dispatchers.Main){
                _listNdefPayload.postValue(response)
            }
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