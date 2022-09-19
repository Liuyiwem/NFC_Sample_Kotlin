package com.example.nfc_sample_kotlin.View

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.nfc_sample_kotlin.Base.BaseFragment
import com.example.nfc_sample_kotlin.TAG
import com.example.nfc_sample_kotlin.ViewModel.ActivityViewModel
import com.example.nfc_sample_kotlin.databinding.FragmentWriteBinding


class WriteFragment : BaseFragment<FragmentWriteBinding>(FragmentWriteBinding::inflate) {

    private val viewModel : ActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.newIntent.observe(viewLifecycleOwner,{
            Log.d(TAG, "onViewCreated: ${it.hashCode()}")})

    }

}