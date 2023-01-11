package com.example.nfc_sample_kotlin.usecase.write

import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.repository.WriteDataRepository

class DeleteWriteDataUseCase(private val writeDataRepository: WriteDataRepository) {

    operator fun invoke(index: Int): List<Message>{
        return writeDataRepository.deleteWriteData(index)
    }
}