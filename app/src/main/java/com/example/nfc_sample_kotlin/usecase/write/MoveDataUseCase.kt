package com.example.nfc_sample_kotlin.usecase.write

import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.repository.WriteDataRepository

class MoveDataUseCase(private val writeDataRepository: WriteDataRepository) {

    operator fun invoke(startPosition: Int, endPosition: Int): List<Message> {
        return writeDataRepository.moveData(startPosition, endPosition)
    }
}