package com.example.nfc_sample_kotlin.View

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nfc_sample_kotlin.Adapter.OnScanDataTouchListener
import com.example.nfc_sample_kotlin.Adapter.ScanDataAdapter
import com.example.nfc_sample_kotlin.Base.BaseFragment
import com.example.nfc_sample_kotlin.TAG
import com.example.nfc_sample_kotlin.ViewModel.ActivityViewModel
import com.example.nfc_sample_kotlin.ViewModel.ScanFragmentViewModel
import com.example.nfc_sample_kotlin.databinding.FragmentScanBinding


class ScanFragment() : BaseFragment<FragmentScanBinding>(FragmentScanBinding::inflate) {

    private val viewModel: ActivityViewModel by activityViewModels()
    private lateinit var scanDataAdapter: ScanDataAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    override fun onResume() {
        super.onResume()
        initObserver()

    }

    private fun initView() {
        scanDataAdapter = ScanDataAdapter()
        binding.rvScan.adapter = scanDataAdapter
        binding.rvScan.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        scanDataAdapter.onScanDataTouchListener = object : OnScanDataTouchListener {
            override fun onTouch(message: String, position: Int, view: View, event: MotionEvent) {
                if (event.action == MotionEvent.ACTION_DOWN && message.length > 100) {
                    view.parent.requestDisallowInterceptTouchEvent(true)

                }
                if (event.action == MotionEvent.ACTION_UP) {
                    Log.d(TAG, "onClick: $position")

                }
            }
        }
    }

    private fun initObserver() {
        viewModel.newIntent.observe(viewLifecycleOwner) {
            viewModel.parseNdefMessage(it)
            binding.tvInit.visibility = GONE
            binding.ivInit.visibility = GONE

            Log.d(TAG, "ndefTag.observe: ${it.hashCode()}")
        }

        viewModel.listNdefPayload.observe(viewLifecycleOwner) {
            scanDataAdapter.listNdefPayload = it
            scanDataAdapter.notifyDataSetChanged()
        }
    }


}
