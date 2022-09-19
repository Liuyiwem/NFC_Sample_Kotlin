package com.example.nfc_sample_kotlin.ViewModel

import android.content.Intent
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nfc_sample_kotlin.Model.Message
import com.example.nfc_sample_kotlin.Repository.Repository

class ActivityViewModel : ViewModel() {
    private val repository = Repository()

    private val _newIntent = MutableLiveData<Intent> ()
    val newIntent : LiveData<Intent> get() = _newIntent

    private val _listNdefPayload = MutableLiveData<List<Message>>()
    val listNdefPayload : LiveData<List<Message>> get() = _listNdefPayload

    fun setNewIntent(intent: Intent){
        _newIntent.value = intent
    }

    fun parseNdefMessage(intent: Intent){
        _listNdefPayload.value = repository.parseNdefMessage(intent)
    }






}