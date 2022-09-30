package com.example.nfc_sample_kotlin.ViewModel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfc_sample_kotlin.Model.Message
import com.example.nfc_sample_kotlin.Repository.ScanDataRepository
import com.example.nfc_sample_kotlin.Repository.ScanDataScanDataRepositoryImpl
import com.example.nfc_sample_kotlin.logi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanFragmentViewModel(private val scanDataRepository: ScanDataRepository = ScanDataScanDataRepositoryImpl()) : ViewModel(){

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