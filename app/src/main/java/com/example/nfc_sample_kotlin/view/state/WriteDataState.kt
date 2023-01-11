package com.example.nfc_sample_kotlin.util

enum class WriteDataState {
    WriteSuccess, WriteFail, TagReadOnly, OverSize, NullRecord, ConnectFail, WrongFormat, GetTagFail
}