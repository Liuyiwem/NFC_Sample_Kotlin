package com.example.nfc_sample_kotlin.Model

import com.example.nfc_sample_kotlin.RecordType

data class Message(val recordType: RecordType, val message: String) {
}