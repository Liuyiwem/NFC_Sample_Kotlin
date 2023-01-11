package com.example.nfc_sample_kotlin.view.state

enum class WriteDataState {
    WriteSuccess, WriteFail, TagReadOnly, OverSize, NullRecord, ConnectFail, WrongFormat, GetTagFail
}