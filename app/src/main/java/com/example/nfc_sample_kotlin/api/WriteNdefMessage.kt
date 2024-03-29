package com.example.nfc_sample_kotlin.api

import android.content.Intent
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.util.RecordType
import com.example.nfc_sample_kotlin.view.state.WriteDataState

interface WriteNdefMessage {

    fun saveWriteData(recordType: RecordType, writeData: String): List<Message>

    fun deleteWriteData(index: Int): List<Message>

    fun moveWriteData(startPosition: Int, endPosition: Int): List<Message>

    fun editWriteData(position: Int, recordType: RecordType, editItemData: String): List<Message>

    fun getWriteDate(): List<Message>

    suspend fun writeTag(intent: Intent) : WriteDataState
}