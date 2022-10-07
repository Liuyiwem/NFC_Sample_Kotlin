package com.example.nfc_sample_kotlin.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.databinding.ItemMessageBinding

class DataViewHolder(private val binding: ItemMessageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var onDataTouchListener: OnDataTouchListener? = null

    fun bind(item: Message, position: Int) {

        binding.tvScanData.text = item.message
        binding.sv.setOnTouchListener(View.OnTouchListener { v, event ->
            onDataTouchListener?.onTouch(item, position, v, event)

            return@OnTouchListener false

        })
    }

}