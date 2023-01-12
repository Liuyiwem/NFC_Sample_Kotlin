package com.example.nfc_sample_kotlin.repository

import android.content.Intent
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.util.RecordType
import com.example.nfc_sample_kotlin.view.state.WriteDataState
import com.example.nfc_sample_kotlin.api.WriteNdefMessage
import java.util.*

class WriteDataRepositoryImpl(private val writeNdefMessage: WriteNdefMessage) :
    WriteDataRepository {

//    private val _writeDataList: MutableList<Message> = mutableListOf()

    override fun saveData(recordType: RecordType, writeData: String): List<Message> {
        return writeNdefMessage.saveWriteData(recordType, writeData)
//        _writeDataList.add(Message(recordType, writeData))
//        return _writeDataList.toList()
    }

    override fun deleteData(index: Int): List<Message> {
        return writeNdefMessage.deleteWriteData(index)
//        _writeDataList.removeAt(index)
//        return _writeDataList.toList()
    }

    override fun moveData(startPosition: Int, endPosition: Int): List<Message> {
        return writeNdefMessage.moveWriteData(startPosition, endPosition)
//        Collections.swap(_writeDataList, startPosition, endPosition)
//        return _writeDataList.toList()
    }

    override fun editData(
        position: Int,
        recordType: RecordType,
        editItemData: String
    ): List<Message> {
        return writeNdefMessage.editWriteData(position, recordType, editItemData)
//        _writeDataList[position] = Message(recordType, editItemData)
//        return _writeDataList.toList()
    }

    override fun getSavedData(): List<Message> {
       return writeNdefMessage.getWriteDate()
    }

    override suspend fun writeData(intent: Intent): WriteDataState {
        return writeNdefMessage.writeTag(intent)

//        return writeNdefMessage.writeTag(intent, _writeDataList.toList())
    }
}