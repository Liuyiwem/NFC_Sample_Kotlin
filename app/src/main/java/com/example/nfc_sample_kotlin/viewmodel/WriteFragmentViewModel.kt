package com.example.nfc_sample_kotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.view.state.WriteDataState
import com.example.nfc_sample_kotlin.logi
import com.example.nfc_sample_kotlin.usecase.WriteDataUseCases
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class WriteFragmentViewModel(private val writeDataUseCases: WriteDataUseCases) :
    ViewModel() {

    private val _writeData = MutableLiveData<List<Message>>()
    val writeData: LiveData<List<Message>> get() = _writeData

    private val _writeNdefResult = MutableSharedFlow<WriteDataState>(replay = 0)
    val writeNdefResult = _writeNdefResult.asSharedFlow()

    init {
        logi("writeViewOnCreated: ")
    }

    fun onEvent(event: WriteDataEvent) {
        when (event) {
            is WriteDataEvent.SaveWriteData -> _writeData.value =
                writeDataUseCases.saveDataUseCase(
                    event.recordType,
                    event.writeData
                )

            is WriteDataEvent.DeleteWriteData -> _writeData.value =
                writeDataUseCases.deleteDataUseCase(event.index)

            is WriteDataEvent.MoveWriteData -> _writeData.value =
                writeDataUseCases.moveDataUseCase(
                    event.startPosition,
                    event.endPosition
                )

            is WriteDataEvent.EditWriteData -> _writeData.value =
                writeDataUseCases.editDataUseCase(
                    event.position,
                    event.recordType,
                    event.editItemData
                )

            is WriteDataEvent.GetSavedData -> _writeData.value =
                writeDataUseCases.getSavedDataUseCase()

            is WriteDataEvent.WriteData -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val response = writeDataUseCases.writeDataUseCase(event.intent)
                    withContext(Dispatchers.Main) {
                        _writeNdefResult.emit(response)
                    }
                }
            }
        }
    }

    override fun onCleared() {
        logi("onCleared: ")
        super.onCleared()
    }
}