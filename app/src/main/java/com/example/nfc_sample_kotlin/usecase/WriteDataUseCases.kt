package com.example.nfc_sample_kotlin.usecase

import com.example.nfc_sample_kotlin.usecase.write.*

data class WriteDataUseCases(
    val saveWriteDataUseCase: SaveWriteDataUseCase,
    val deleteWriteDataUseCase: DeleteWriteDataUseCase,
    val moveWriteDataUseCase: MoveWriteDataUseCase,
    val editWriteDataUseCase: EditWriteDataUseCase,
    val writeSavedDataUseCase: WriteSavedDataUseCase
    )
