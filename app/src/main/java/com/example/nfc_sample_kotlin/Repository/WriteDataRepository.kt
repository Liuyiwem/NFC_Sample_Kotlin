package com.example.nfc_sample_kotlin.Repository

import android.content.Intent
import com.example.nfc_sample_kotlin.Model.Message
import com.example.nfc_sample_kotlin.RecordType

interface WriteDataRepository {

    fun saveWriteData(recordType: RecordType, writeData: String): List<Message>

    fun deleteWriteData(index: Int): List<Message>

    fun moveWriteData(startPosition: Int, endPosition: Int): List<Message>

    suspend fun writeSavedData(intent: Intent):Boolean


}