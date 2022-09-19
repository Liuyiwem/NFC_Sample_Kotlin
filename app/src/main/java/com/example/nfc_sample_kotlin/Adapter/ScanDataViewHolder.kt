package com.example.nfc_sample_kotlin.Adapter

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.nfc_sample_kotlin.Model.Message
import com.example.nfc_sample_kotlin.databinding.ItemScanBinding

class ScanDataViewHolder(private val binding: ItemScanBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var onScanDataTouchListener: OnScanDataTouchListener? = null

    fun bind(item: Message, position: Int) {

        binding.tvScanData.text = item.message
        binding.sv.setOnTouchListener(View.OnTouchListener { v, event ->
            onScanDataTouchListener?.onTouch(item.message, position, v, event)

            return@OnTouchListener false

        })
    }

}