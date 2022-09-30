package com.example.nfc_sample_kotlin.Repository

import android.content.Intent
import com.example.nfc_sample_kotlin.Model.Message
import com.example.nfc_sample_kotlin.RecordType
import com.example.nfc_sample_kotlin.api.WriteNdefMessage
import com.example.nfc_sample_kotlin.api.WriteNdefMessageImpl
import java.util.*

class WriteDataRepositoryImpl(private val writeNdefMessage: WriteNdefMessage = WriteNdefMessageImpl()) :
    WriteDataRepository {

    private val _writeDataList: MutableList<Message> = mutableListOf()
    var writeDataList: List<Message> = _writeDataList

    override fun saveWriteData(recordType: RecordType, writeData: String): List<Message> {
        _writeDataList.add(Message(recordType, writeData))
        return writeDataList
    }

    override fun deleteWriteData(index: Int): List<Message> {
        _writeDataList.removeAt(index)
        return writeDataList
    }

    override fun moveWriteData(startPosition: Int, endPosition: Int): List<Message> {
        Collections.swap(_writeDataList,startPosition,endPosition)
        return writeDataList
    }

    override suspend fun writeSavedData(intent: Intent): Boolean {
        return writeNdefMessage.writeTag(intent, writeDataList)


    }
}