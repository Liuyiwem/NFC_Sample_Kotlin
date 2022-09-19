package com.example.nfc_sample_kotlin.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nfc_sample_kotlin.Model.Message
import com.example.nfc_sample_kotlin.databinding.ItemScanBinding

class ScanDataAdapter : RecyclerView.Adapter<ScanDataViewHolder>() {

    var onScanDataTouchListener: OnScanDataTouchListener? = null
    var listNdefPayload: List<Message> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanDataViewHolder {

        val binding = ItemScanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScanDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScanDataViewHolder, position: Int) {
        holder.bind(listNdefPayload[position], position)
        holder.onScanDataTouchListener = onScanDataTouchListener

        

    }

    override fun getItemCount(): Int {
        return listNdefPayload.size

    }


}