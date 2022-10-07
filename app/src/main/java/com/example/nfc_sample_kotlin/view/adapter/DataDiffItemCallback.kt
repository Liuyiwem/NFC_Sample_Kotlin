package com.example.nfc_sample_kotlin.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.nfc_sample_kotlin.model.Message

class DataDiffItemCallback: DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.message == newItem.message
    }
}