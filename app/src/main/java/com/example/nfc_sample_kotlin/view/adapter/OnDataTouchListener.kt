package com.example.nfc_sample_kotlin.view.adapter

import android.view.MotionEvent
import android.view.View
import com.example.nfc_sample_kotlin.model.Message

interface OnDataTouchListener {
    fun onTouch(item: Message, position: Int,view :View,event: MotionEvent)
}