package com.example.nfc_sample_kotlin.Repository

import android.content.Intent
import android.os.Parcelable
import com.example.nfc_sample_kotlin.Model.Message
import com.example.nfc_sample_kotlin.Model.ParseNdefMessage

class Repository {

    fun parseNdefMessage(intent: Intent): List<Message> {
        return ParseNdefMessage.parseToMessage(intent)
    }



}