package com.example.nfc_sample_kotlin.repository

import android.content.Intent
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.util.RecordType
import com.example.nfc_sample_kotlin.view.state.WriteDataState
import com.example.nfc_sample_kotlin.api.WriteNdefMessage
import java.util.*

class WriteDataRepositoryImpl(private val writeNdefMessage: WriteNdefMessage) :
    WriteDataRepository {

    private val _writeDataList: MutableList<Message> = mutableListOf()

    override fun saveWriteData(recordType: RecordType, writeData: String): List<Message> {
        _writeDataList.add(Message(recordType, writeData))
        return _writeDataList.toList()
    }

    override fun deleteWriteData(index: Int): List<Message> {
        _writeDataList.removeAt(index)
        return _writeDataList.toList()
    }

    override fun moveWriteData(startPosition: Int, endPosition: Int): List<Message> {
        Collections.swap(_writeDataList,startPosition,endPosition)
        return _writeDataList.toList()
    }

    override fun editWriteData(
        position: Int,
        recordType: RecordType,
        editItemData: String
    ): List<Message> {
        _writeDataList[position] = Message(recordType,editItemData)
        return _writeDataList.toList()
    }

    override suspend fun writeSavedData(intent: Intent): WriteDataState {
        return writeNdefMessage.writeTag(intent, _writeDataList.toList())
    }
}