package com.example.nfc_sample_kotlin.Repository

import android.content.Intent
import com.example.nfc_sample_kotlin.Model.Message

interface ScanDataRepository {

   suspend fun parseNdefMessage(intent: Intent): List<Message>
}