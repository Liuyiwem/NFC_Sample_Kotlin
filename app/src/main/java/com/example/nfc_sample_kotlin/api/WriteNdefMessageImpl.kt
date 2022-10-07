package com.example.nfc_sample_kotlin.api

import android.content.Intent
import android.nfc.*
import android.nfc.tech.Ndef
import android.os.RemoteException
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.enum.RecordType
import com.example.nfc_sample_kotlin.enum.WriteDataState
import java.io.IOException


class WriteNdefMessageImpl : WriteNdefMessage {

    @Synchronized
    override suspend fun writeTag(intent: Intent, writeDataList: List<Message>): WriteDataState {

        try {
            val ndefMessage = combineNdefMessage(writeDataList)
            val tag = getNdefTag(intent)
            val ndef = Ndef.get(tag)
            if (ndef != null) {

                try {
                    if (!ndef.isWritable) {
                        return WriteDataState.TagReadOnly

                    }
                    if (ndef.maxSize < ndefMessage.byteArrayLength) {
                        return WriteDataState.OverSize

                    }
                    if (!ndef.isConnected) {
                        ndef.connect()
                        ndef.writeNdefMessage(ndefMessage)
                        return WriteDataState.WriteSuccess

                    }
                } catch (e: IOException) {
                    return WriteDataState.ConnectFail

                } catch (e: FormatException) {
                    return WriteDataState.WrongFormat
                } finally {
                    ndef.close()
                }
            }
        } catch (e: RemoteException) {
            return WriteDataState.GetTagFail
        }catch (e: KotlinNullPointerException){
            return WriteDataState.NullRecord
        }

        return WriteDataState.WriteFail
    }

    private fun combineNdefMessage(writeDataList: List<Message>): NdefMessage {
        if (writeDataList.isEmpty()){
            throw KotlinNullPointerException("Ndef record is empty")
        }

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

    private fun getNdefTag(intent: Intent): Tag {

        return intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)!!
    }
}