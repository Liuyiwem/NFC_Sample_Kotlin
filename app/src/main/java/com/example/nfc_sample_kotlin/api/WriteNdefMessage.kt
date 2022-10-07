package com.example.nfc_sample_kotlin.api

import android.content.Intent
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.enum.WriteDataState

interface WriteNdefMessage {

    suspend fun writeTag(intent: Intent, writeDataList: List<Message>) : WriteDataState
}