package com.example.nfc_sample_kotlin.usecase.write

import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.repository.WriteDataRepository

class DeleteDataUseCase(private val writeDataRepository: WriteDataRepository) {

    operator fun invoke(index: Int): List<Message>{
        return writeDataRepository.deleteData(index)
    }
}