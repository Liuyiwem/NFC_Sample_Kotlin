package com.example.nfc_sample_kotlin.ViewModel

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.nfc_sample_kotlin.TAG

class ScanFragmentViewModel : ViewModel(){

    init {
        Log.d(TAG, "scanViewOnCreated: ")
    }


    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: ")
    }
}