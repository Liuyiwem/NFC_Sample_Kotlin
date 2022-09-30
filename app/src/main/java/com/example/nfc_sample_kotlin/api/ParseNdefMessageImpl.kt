package com.example.nfc_sample_kotlin.api

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import com.example.nfc_sample_kotlin.Model.Message
import com.example.nfc_sample_kotlin.RecordType
import kotlin.experimental.and


class ParseNdefMessageImpl : ParseNdefMessage {

    companion object{

        val URI_PREFIX_MAP: Array<String> = arrayOf(
            "", // 0x00
            "http://www.", // 0x01
            "https://www.", // 0x02
            "http://", // 0x03
            "https://", // 0x04
            "tel:", // 0x05
            "mailto:", // 0x06
            "ftp://anonymous:anonymous@", // 0x07
            "ftp://ftp.", // 0x08
            "ftps://", // 0x09
            "sftp://", // 0x0A
            "smb://", // 0x0B
            "nfs://", // 0x0C
            "ftp://", // 0x0D
            "dav://", // 0x0E
            "news:", // 0x0F
            "telnet://", // 0x10
            "imap:", // 0x11
            "rtsp://", // 0x12
            "urn:", // 0x13
            "pop:", // 0x14
            "sip:", // 0x15
            "sips:", // 0x16
            "tftp:", // 0x17
            "btspp://", // 0x18
            "btl2cap://", // 0x19
            "btgoep://", // 0x1A
            "tcpobex://", // 0x1B
            "irdaobex://", // 0x1C
            "file://", // 0x1D
            "urn:epc:id:", // 0x1E
            "urn:epc:tag:", // 0x1F
            "urn:epc:pat:", // 0x20
            "urn:epc:raw:", // 0x21
            "urn:epc:", // 0x22
        )

    }

    override suspend fun parseToMessage(intent: Intent): List<Message> {

        val listNdefPayload: MutableList<Message> = mutableListOf()
        (parseIntent(intent).records).forEach {
            if (it.tnf == NdefRecord.TNF_WELL_KNOWN) {

                if (it.type.contentEquals(NdefRecord.RTD_TEXT)) {
                    listNdefPayload.add(Message(RecordType.Text,parseRTDText(it.payload)))
                }

                if (it.type.contentEquals(NdefRecord.RTD_URI)) {
                    listNdefPayload.add(Message(RecordType.Uri,parseRTDURI(it.payload)))
                }

            }
        }
        return listNdefPayload
    }

    private fun parseIntent(intent: Intent): NdefMessage {

        return intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            .let { it?.get(0) as NdefMessage }
    }

    private fun parseRTDText(payload: ByteArray): String {

        val encoding =
            if ((payload[0].toInt() and 0x80) == 0x00) Charsets.UTF_8 else Charsets.UTF_16
        val languageCodeLength = (payload[0] and 0x3F).toInt()
        return String(
            payload,
            languageCodeLength + 1,
            payload.size - languageCodeLength - 1,
            encoding
        )
    }

    private fun parseRTDURI(payload: ByteArray): String {
        val prefix = URI_PREFIX_MAP[(payload[0].toInt() and 0xff)]
        val uri = String(payload, 1, payload.size - 1, Charsets.UTF_8)
        return prefix + uri
    }


}