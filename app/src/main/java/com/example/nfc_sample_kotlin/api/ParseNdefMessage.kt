package com.example.nfc_sample_kotlin.api

import android.content.Intent
import com.example.nfc_sample_kotlin.Model.Message

interface ParseNdefMessage {
   suspend fun parseToMessage(intent: Intent): List<Message>
}