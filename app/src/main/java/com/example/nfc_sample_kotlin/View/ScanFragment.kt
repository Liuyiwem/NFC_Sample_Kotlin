package com.example.nfc_sample_kotlin.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nfc_sample_kotlin.View.Adapter.DataAdapter
import com.example.nfc_sample_kotlin.View.Adapter.OnDataTouchListener
import com.example.nfc_sample_kotlin.ViewModel.ActivityViewModel
import com.example.nfc_sample_kotlin.ViewModel.ScanFragmentViewModel
import com.example.nfc_sample_kotlin.databinding.FragmentScanBinding
import com.example.nfc_sample_kotlin.logd
import com.example.nfc_sample_kotlin.logi
import kotlinx.coroutines.launch


class ScanFragment() : Fragment() {
    private val viewModel: ActivityViewModel by activityViewModels()
    private val scanViewModel: ScanFragmentViewModel by viewModels()
    private val scanDataAdapter: DataAdapter = DataAdapter()
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logi("onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanBinding.inflate(inflater, container, false)

        if (scanDataAdapter.listNdefPayload.isNotEmpty()) {
            binding.tvInit.visibility = INVISIBLE
            binding.ivInit.visibility = INVISIBLE
        }
        logi("onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initCollect()
        logi("onViewCreated")

    }

    override fun onResume() {
        super.onResume()
        initObserver()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    override fun onDestroy() {
        super.onDestroy()
        logi("onDestroy")
    }

    private fun initView() {
        binding.rvScan.adapter = scanDataAdapter
        binding.rvScan.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        scanDataAdapter.onDataTouchListener = object : OnDataTouchListener {
            override fun onTouch(message: String, position: Int, view: View, event: MotionEvent) {
                if (event.action == MotionEvent.ACTION_DOWN && message.length > 100) {
                    view.parent.requestDisallowInterceptTouchEvent(true)

                }
                if (event.action == MotionEvent.ACTION_UP) {
                    logd("onClick: $position")

                }
            }
        }
    }

    private fun initObserver() {
        scanViewModel.listNdefPayload.observe(viewLifecycleOwner) {
            scanDataAdapter.listNdefPayload = it
            scanDataAdapter.notifyDataSetChanged()
            binding.tvInit.visibility = INVISIBLE
            binding.ivInit.visibility = INVISIBLE

        }
    }

    private fun initCollect(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newIntent.collect {
                    scanViewModel.parseNdefMessage(it)
                    logi("scanFragmentGetIntent: ${it.hashCode()}")

                }
            }
        }
    }

}
