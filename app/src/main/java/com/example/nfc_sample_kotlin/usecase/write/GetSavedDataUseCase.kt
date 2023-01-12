package com.example.nfc_sample_kotlin.usecase.write

import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.repository.WriteDataRepository

class GetSavedDataUseCase(private val writeDataRepository: WriteDataRepository) {

    operator fun invoke(): List<Message> {
        return writeDataRepository.getSavedData()
    }
}