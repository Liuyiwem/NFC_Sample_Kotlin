package com.example.nfc_sample_kotlin.enum

enum class WriteDataState {
    WriteSuccess, WriteFail, TagReadOnly, OverSize, NullRecord, ConnectFail, WrongFormat, GetTagFail
}