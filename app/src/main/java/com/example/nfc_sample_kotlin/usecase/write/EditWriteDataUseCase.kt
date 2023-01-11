package com.example.nfc_sample_kotlin.usecase.write

import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.repository.WriteDataRepository
import com.example.nfc_sample_kotlin.util.RecordType

class EditWriteDataUseCase(private val writeDataRepository: WriteDataRepository) {

    operator fun invoke(position: Int,
                        recordType: RecordType,
                        editItemData: String
    ): List<Message> {
        return writeDataRepository.editWriteData(position,recordType,editItemData)
    }
}