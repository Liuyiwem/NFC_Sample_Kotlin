package com.example.nfc_sample_kotlin.repository

import android.content.Intent
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.util.RecordType
import com.example.nfc_sample_kotlin.view.state.WriteDataState

interface WriteDataRepository {

    fun saveData(recordType: RecordType, writeData: String): List<Message>

    fun deleteData(index: Int): List<Message>

    fun moveData(startPosition: Int, endPosition: Int): List<Message>

    fun editData(position: Int, recordType: RecordType, editItemData: String): List<Message>

    fun getSavedData(): List<Message>

    suspend fun writeData(intent: Intent): WriteDataState


}