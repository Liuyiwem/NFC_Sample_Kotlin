package com.example.nfc_sample_kotlin.api

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import com.example.nfc_sample_kotlin.Model.Message
import com.example.nfc_sample_kotlin.RecordType


class WriteNdefMessageImpl : WriteNdefMessage {

    @Synchronized
    override suspend fun writeTag(intent: Intent, writeDataList: List<Message>): Boolean {

        val ndefMessage = combineNdefMessage(writeDataList)
        val tag = getNdefTag(intent)

                try {
                    val ndef = Ndef.get(tag)

                    try {
                        if (!ndef.isConnected) {
                            ndef.connect()
                            ndef.writeNdefMessage(ndefMessage)
                            return true

                        }
                        if (!ndef.isWritable) {
                            return false

                        }
                        if (ndef.maxSize < ndefMessage.byteArrayLength) {
                            return false

                        }
                    } catch (e: Exception) {
                        return false

                    } finally {
                        ndef.close()
                    }
                } catch (e: Exception) {
                    return false
                }

        return false
    }

    private fun combineNdefMessage(writeDataList: List<Message>): NdefMessage {

        val ndefRecords = mutableListOf<NdefRecord>()
        writeDataList.forEach { Message ->

            if (Message.recordType == RecordType.Text) {
                ndefRecords.add(NdefRecord.createTextRecord("en", Message.message))

            }
            if (Message.recordType == RecordType.Uri) {
                ndefRecords.add(NdefRecord.createUri(Message.message))

            }
        }
        return NdefMessage(ndefRecords.toTypedArray())

    }

    private fun getNdefTag(intent: Intent): Tag? {

        return intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
    }
}