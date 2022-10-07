package com.example.nfc_sample_kotlin.model

import com.example.nfc_sample_kotlin.enum.RecordType

data class Message(val id: Int, val recordType: RecordType, val message: String) {
}