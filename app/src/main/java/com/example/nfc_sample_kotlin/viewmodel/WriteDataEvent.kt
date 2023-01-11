package com.example.nfc_sample_kotlin.viewmodel

import android.content.Intent
import com.example.nfc_sample_kotlin.util.RecordType

sealed class WriteDataEvent{
    data class SaveWriteData(val recordType: RecordType,val writeData: String): WriteDataEvent()
    data class DeleteWriteData(val index: Int): WriteDataEvent()
    data class MoveWriteData(val startPosition: Int, val endPosition: Int): WriteDataEvent()
    data class EditWriteData(val position: Int, val recordType: RecordType, val editItemData: String): WriteDataEvent()
    data class WriteSavedData(val intent: Intent): WriteDataEvent()
}
