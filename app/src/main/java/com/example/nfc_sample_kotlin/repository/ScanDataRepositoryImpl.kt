package com.example.nfc_sample_kotlin.repository

import android.content.Intent
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.api.ParseNdefMessage
import kotlinx.coroutines.flow.Flow

class ScanDataRepositoryImpl(private val parseNdefMessage: ParseNdefMessage) : ScanDataRepository {

    override suspend fun parseNdefMessage(intent: Intent): Flow<List<Message>> {
        return parseNdefMessage.parseToMessage(intent)
    }

}