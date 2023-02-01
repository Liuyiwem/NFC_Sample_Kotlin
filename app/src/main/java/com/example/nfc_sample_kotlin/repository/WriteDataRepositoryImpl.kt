package com.example.nfc_sample_kotlin.repository

import android.content.Intent
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.util.RecordType
import com.example.nfc_sample_kotlin.view.state.WriteDataState
import com.example.nfc_sample_kotlin.api.WriteNdefMessage

class WriteDataRepositoryImpl(private val writeNdefMessage: WriteNdefMessage) :
    WriteDataRepository {

    override fun saveData(recordType: RecordType, writeData: String): List<Message> {
        return writeNdefMessage.saveWriteData(recordType, writeData)
    }

    override fun deleteData(index: Int): List<Message> {
        return writeNdefMessage.deleteWriteData(index)
    }

    override fun moveData(startPosition: Int, endPosition: Int): List<Message> {
        return writeNdefMessage.moveWriteData(startPosition, endPosition)
    }

    override fun editData(
        position: Int,
        recordType: RecordType,
        editItemData: String
    ): List<Message> {
        return writeNdefMessage.editWriteData(position, recordType, editItemData)
    }

    override fun getSavedData(): List<Message> {
       return writeNdefMessage.getWriteDate()
    }

    override suspend fun writeData(intent: Intent): WriteDataState {
        return writeNdefMessage.writeTag(intent)

    }
}