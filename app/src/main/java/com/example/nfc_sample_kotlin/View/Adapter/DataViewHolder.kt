package com.example.nfc_sample_kotlin.View.Adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.nfc_sample_kotlin.Model.Message
import com.example.nfc_sample_kotlin.databinding.ItemMessageBinding
import com.example.nfc_sample_kotlin.databinding.ItemMmBinding

class DataViewHolder(private val binding: ItemMmBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var onDataTouchListener: OnDataTouchListener? = null

    fun bind(item: Message, position: Int) {

        binding.tvScanData.text = item.message
        binding.sv.setOnTouchListener(View.OnTouchListener { v, event ->
            onDataTouchListener?.onTouch(item.message, position, v, event)

            return@OnTouchListener false

        })
    }

}