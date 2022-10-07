package com.example.nfc_sample_kotlin.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.view.adapter.DataAdapter
import com.example.nfc_sample_kotlin.view.adapter.OnDataTouchListener
import com.example.nfc_sample_kotlin.viewmodel.ActivityViewModel
import com.example.nfc_sample_kotlin.viewmodel.ScanFragmentViewModel
import com.example.nfc_sample_kotlin.databinding.FragmentScanBinding
import com.example.nfc_sample_kotlin.logi
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ScanFragment() : Fragment() {
    private val viewModel by sharedViewModel<ActivityViewModel>()
    private val scanViewModel by viewModel<ScanFragmentViewModel>()
    private val scanDataAdapter: DataAdapter = DataAdapter()
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private var scanDataList: List<Message> = mutableListOf()


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

        if (scanDataList.isNotEmpty()) {
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
            override fun onTouch(item: Message, position: Int, view: View, event: MotionEvent) {
                if (event.action == MotionEvent.ACTION_DOWN && item.message.length > 50) {
                    view.parent.requestDisallowInterceptTouchEvent(true)

                }
            }
        }
    }

    private fun initObserver() {
        scanViewModel.listNdefPayload.observe(viewLifecycleOwner) {
            scanDataList = it
            scanDataAdapter.submitList(it)
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
