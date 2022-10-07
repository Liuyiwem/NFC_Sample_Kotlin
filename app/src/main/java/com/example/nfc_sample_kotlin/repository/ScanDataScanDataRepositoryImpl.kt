package com.example.nfc_sample_kotlin.repository

import android.content.Intent
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.api.ParseNdefMessage

class ScanDataScanDataRepositoryImpl(private val parseNdefMessage: ParseNdefMessage) : ScanDataRepository {

    override suspend fun parseNdefMessage(intent: Intent): List<Message> {
        return parseNdefMessage.parseToMessage(intent)
    }


}