package com.example.nfc_sample_kotlin.Base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.nfc_sample_kotlin.logi

typealias ActivityInflate<T> = (LayoutInflater) -> T

abstract class BaseActivity<VB : ViewBinding>(
    private val inflate: ActivityInflate<VB>
) : AppCompatActivity() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = inflate.invoke(layoutInflater)
        setContentView(binding.root)
        logi("onCreate: ${this.hashCode()}")

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

