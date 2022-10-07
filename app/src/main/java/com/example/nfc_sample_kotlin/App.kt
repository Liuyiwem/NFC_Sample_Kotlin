package com.example.nfc_sample_kotlin

import android.app.Application
import com.example.nfc_sample_kotlin.di.DiModule

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        DiModule().init()
    }
}