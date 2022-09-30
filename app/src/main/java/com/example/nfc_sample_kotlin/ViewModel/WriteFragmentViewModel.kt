package com.example.nfc_sample_kotlin.ViewModel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfc_sample_kotlin.Model.Message
import com.example.nfc_sample_kotlin.RecordType
import com.example.nfc_sample_kotlin.Repository.WriteDataRepository
import com.example.nfc_sample_kotlin.Repository.WriteDataRepositoryImpl
import com.example.nfc_sample_kotlin.TAG
import com.example.nfc_sample_kotlin.logi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class WriteFragmentViewModel(private val writeDataRepository: WriteDataRepository = WriteDataRepositoryImpl()) :
    ViewModel() {

    private val _writeData = MutableLiveData<List<Message>>()
    val writeData: LiveData<List<Message>> get() = _writeData

    private val _writeNdefResult = MutableSharedFlow<Boolean>(replay = 0)
    val writeNdefResult = _writeNdefResult.asSharedFlow()

    fun saveWriteData(recordType: RecordType, writeData: String) {

        _writeData.value = writeDataRepository.saveWriteData(recordType, writeData)
    }

    fun deleteWriteData(index: Int) {
        _writeData.value = writeDataRepository.deleteWriteData(index)
    }

    fun moveWriteData(startPosition: Int, endPosition: Int) {
        _writeData.value = writeDataRepository.moveWriteData(startPosition, endPosition)

    }

    fun writeSavedData(intent: Intent) {

        viewModelScope.launch(Dispatchers.IO) {

            val response = writeDataRepository.writeSavedData(intent)
            withContext(Dispatchers.Main) {
                _writeNdefResult.emit(response)
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