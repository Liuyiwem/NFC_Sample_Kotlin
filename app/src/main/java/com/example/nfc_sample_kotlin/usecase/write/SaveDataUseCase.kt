package com.example.nfc_sample_kotlin.usecase.write

import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.repository.WriteDataRepository
import com.example.nfc_sample_kotlin.util.RecordType

class SaveDataUseCase(private val writeDataRepository: WriteDataRepository) {

    operator fun invoke(recordType: RecordType, writeData: String): List<Message> {
        return writeDataRepository.saveData(recordType, writeData)
    }
}