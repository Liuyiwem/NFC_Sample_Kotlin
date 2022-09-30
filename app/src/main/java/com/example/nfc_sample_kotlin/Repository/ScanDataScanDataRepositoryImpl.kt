package com.example.nfc_sample_kotlin.Repository

import android.content.Intent
import com.example.nfc_sample_kotlin.Model.Message
import com.example.nfc_sample_kotlin.api.ParseNdefMessageImpl
import com.example.nfc_sample_kotlin.api.ParseNdefMessage

class ScanDataScanDataRepositoryImpl(private val parseNdefMessage: ParseNdefMessage = ParseNdefMessageImpl()) : ScanDataRepository {

    override suspend fun parseNdefMessage(intent: Intent): List<Message> {
        return parseNdefMessage.parseToMessage(intent)
    }


}