package com.example.nfc_sample_kotlin.repository

import android.content.Intent
import com.example.nfc_sample_kotlin.model.Message
import kotlinx.coroutines.flow.Flow

interface ScanDataRepository {

   suspend fun parseNdefMessage(intent: Intent): Flow<List<Message>>
}