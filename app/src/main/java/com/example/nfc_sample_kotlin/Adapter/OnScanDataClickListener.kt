package com.example.nfc_sample_kotlin.Adapter

import android.view.MotionEvent
import android.view.View

interface OnScanDataTouchListener {
    fun onTouch(message: String, position: Int,view :View,event: MotionEvent)
}