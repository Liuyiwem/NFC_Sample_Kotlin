package com.example.nfc_sample_kotlin.View.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nfc_sample_kotlin.Model.Message
import com.example.nfc_sample_kotlin.databinding.ItemMessageBinding
import com.example.nfc_sample_kotlin.databinding.ItemMmBinding

class DataAdapter : RecyclerView.Adapter<DataViewHolder>() {

    var onDataTouchListener: OnDataTouchListener? = null
    var listNdefPayload: List<Message> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {

        val binding = ItemMmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(listNdefPayload[position], position)
        holder.onDataTouchListener = onDataTouchListener

    }

    override fun getItemCount(): Int {
        return listNdefPayload.size

    }

}