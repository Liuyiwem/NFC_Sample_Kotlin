package com.example.nfc_sample_kotlin.repository

import android.content.Intent
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.util.RecordType
import com.example.nfc_sample_kotlin.view.state.WriteDataState

interface WriteDataRepository {

    fun saveWriteData(recordType: RecordType, writeData: String): List<Message>

    fun deleteWriteData(index: Int): List<Message>

    fun moveWriteData(startPosition: Int, endPosition: Int): List<Message>

    fun editWriteData(position: Int, recordType: RecordType, editItemData: String):List<Message>

    suspend fun writeSavedData(intent: Intent): WriteDataState


}