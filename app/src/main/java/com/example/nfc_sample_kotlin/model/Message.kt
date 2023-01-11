package com.example.nfc_sample_kotlin.model

import android.os.Parcelable
import com.example.nfc_sample_kotlin.util.RecordType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(val recordType: RecordType, val message: String) : Parcelable
