package com.example.nfc_sample_kotlin.Base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.nfc_sample_kotlin.logi


//Create a fun
typealias FragmentInflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB: ViewBinding>(
    private val inflate: FragmentInflate<VB>
) : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        logi("onCreateView: ${this::class.java.simpleName}@${this.hashCode()}")
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        logi("onDestroyView: ${this::class.java.simpleName}@${this.hashCode()}")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        logi("onDestroy: ${this::class.java.simpleName}@${this.hashCode()}")
    }

    override fun onDetach() {
        super.onDetach()
        logi("onDetach: ${this::class.java.simpleName}@${this.hashCode()}")
    }

}
