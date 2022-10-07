package com.example.nfc_sample_kotlin.repository

import android.content.Intent
import com.example.nfc_sample_kotlin.model.Message

interface ScanDataRepository {

   suspend fun parseNdefMessage(intent: Intent): List<Message>
}