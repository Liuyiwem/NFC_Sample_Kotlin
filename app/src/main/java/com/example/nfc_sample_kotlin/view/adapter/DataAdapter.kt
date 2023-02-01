package com.example.nfc_sample_kotlin.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.databinding.ItemMessageBinding


class DataAdapter : ListAdapter<Message, DataViewHolder>(DataDiffItemCallback()) {

    var onDataTouchListener: OnDataTouchListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {

        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        holder.bind(getItem(position), position)
        holder.onDataTouchListener = onDataTouchListener
    }
}