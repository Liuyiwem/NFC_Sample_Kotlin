package com.example.nfc_sample_kotlin.api

import android.content.Intent
import android.nfc.*
import android.nfc.tech.Ndef
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.util.RecordType
import com.example.nfc_sample_kotlin.view.state.WriteDataState
import java.io.IOException
import java.util.*


class WriteNdefMessageImpl : WriteNdefMessage {

    private val _writeDataList: MutableList<Message> = mutableListOf()

    override fun saveWriteData(recordType: RecordType, writeData: String): List<Message> {
        _writeDataList.add(Message(recordType, writeData))
        return _writeDataList.toList()
    }

    override fun deleteWriteData(index: Int): List<Message> {
        _writeDataList.removeAt(index)
        return _writeDataList.toList()
    }

    override fun moveWriteData(startPosition: Int, endPosition: Int): List<Message> {
        Collections.swap(_writeDataList, startPosition, endPosition)
        return _writeDataList.toList()
    }

    override fun editWriteData(
        position: Int,
        recordType: RecordType,
        editItemData: String
    ): List<Message> {
        _writeDataList[position] = Message(recordType, editItemData)
        return _writeDataList.toList()
    }

    override fun getWriteDate(): List<Message> {
        return _writeDataList.toList()
    }

    override suspend fun writeTag(intent: Intent): WriteDataState {

        val tag = getNdefTag(intent)
        val ndef = Ndef.get(tag)
        if (ndef != null) {
            try {
                val ndefMessage = combineNdefMessage(_writeDataList)
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
            } catch (e: KotlinNullPointerException) {
                e.printStackTrace()
                return WriteDataState.NullRecord
            } catch (e: IOException) {
                e.printStackTrace()
                return WriteDataState.ConnectFail
            } catch (e: FormatException) {
                e.printStackTrace()
                return WriteDataState.WrongFormat
            } finally {
                ndef.close()
            }
        } else {
            return WriteDataState.GetTagFail
        }
        return WriteDataState.WriteFail
    }

    private fun combineNdefMessage(writeDataList: List<Message>): NdefMessage {
        if (writeDataList.isEmpty()) {
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