package com.example.nfc_sample_kotlin.usecase

import com.example.nfc_sample_kotlin.usecase.write.*

data class WriteDataUseCases(
    val saveDataUseCase: SaveDataUseCase,
    val deleteDataUseCase: DeleteDataUseCase,
    val moveDataUseCase: MoveDataUseCase,
    val editDataUseCase: EditDataUseCase,
    val getSavedDataUseCase: GetSavedDataUseCase,
    val writeDataUseCase: WriteDataUseCase
    )
