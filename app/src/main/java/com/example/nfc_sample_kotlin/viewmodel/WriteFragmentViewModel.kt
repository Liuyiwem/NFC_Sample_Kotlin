package com.example.nfc_sample_kotlin.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.enum.RecordType
import com.example.nfc_sample_kotlin.repository.WriteDataRepository
import com.example.nfc_sample_kotlin.enum.WriteDataState
import com.example.nfc_sample_kotlin.logi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class WriteFragmentViewModel(private val writeDataRepository: WriteDataRepository) :
    ViewModel() {

    private val _writeData = MutableLiveData<List<Message>>()
    val writeData: LiveData<List<Message>> get() = _writeData

    private val _writeNdefResult = MutableSharedFlow<WriteDataState>(replay = 0)
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

    fun editItemData(position: Int, recordType: RecordType, editItemData: String) {
        _writeData.value = writeDataRepository.editWriteData(position,recordType,editItemData)
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
        logi("writeViewOnCreated: ")
    }

    override fun onCleared() {
        super.onCleared()
        logi("onCleared: ")

    }

}