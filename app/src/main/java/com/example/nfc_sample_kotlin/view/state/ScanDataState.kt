package com.example.nfc_sample_kotlin.view.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

typealias StatefulLiveData<T> = LiveData<ScanDataState<T>>
typealias StatefulMutableLiveData<T> = MutableLiveData<ScanDataState<T>>

sealed class ScanDataState <out T>{

    data class Success<T>(val data: T) : ScanDataState<T>()
    data class Failure(val throwable: Throwable) : ScanDataState<Nothing>()
    object Loading : ScanDataState<Nothing>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun failure(throwable: Throwable) = Failure(throwable)
        fun loading() = Loading
    }

}