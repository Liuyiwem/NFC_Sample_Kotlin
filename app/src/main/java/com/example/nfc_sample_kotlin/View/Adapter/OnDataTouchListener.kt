package com.example.nfc_sample_kotlin.View.Adapter

import android.view.MotionEvent
import android.view.View

interface OnDataTouchListener {
    fun onTouch(message: String, position: Int,view :View,event: MotionEvent)
}