package com.example.nfc_sample_kotlin.usecase.write

import android.content.Intent
import com.example.nfc_sample_kotlin.repository.WriteDataRepository
import com.example.nfc_sample_kotlin.view.state.WriteDataState

class WriteSavedDataUseCase(private val writeDataRepository: WriteDataRepository) {

    suspend operator fun invoke(intent: Intent): WriteDataState {
        return writeDataRepository.writeSavedData(intent)
    }
}