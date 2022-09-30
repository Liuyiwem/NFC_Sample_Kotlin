package com.example.nfc_sample_kotlin.api

import android.content.Intent
import com.example.nfc_sample_kotlin.Model.Message

interface WriteNdefMessage {

    suspend fun writeTag(intent: Intent, writeDataList: List<Message>) :Boolean
}