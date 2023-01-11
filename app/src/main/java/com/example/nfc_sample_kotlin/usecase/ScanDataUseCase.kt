package com.example.nfc_sample_kotlin.usecase

import android.content.Intent
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.repository.ScanDataRepository
import kotlinx.coroutines.flow.Flow

class ScanDataUseCase(private val scanDataRepository: ScanDataRepository) {

    suspend operator fun invoke(intent: Intent): Flow<List<Message>> {
        return scanDataRepository.parseNdefMessage(intent)
    }
}