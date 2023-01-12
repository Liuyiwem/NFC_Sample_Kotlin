package com.example.nfc_sample_kotlin.view


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nfc_sample_kotlin.databinding.FragmentScanBinding
import com.example.nfc_sample_kotlin.util.RecordType
import com.example.nfc_sample_kotlin.logi
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.view.state.ScanDataState
import com.example.nfc_sample_kotlin.view.adapter.DataAdapter
import com.example.nfc_sample_kotlin.view.adapter.OnDataTouchListener
import com.example.nfc_sample_kotlin.viewmodel.ActivityViewModel
import com.example.nfc_sample_kotlin.viewmodel.ScanFragmentViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class ScanFragment() : Fragment() {
    private val viewModel by sharedViewModel<ActivityViewModel>()
    private val scanViewModel by viewModel<ScanFragmentViewModel>()
    private val scanDataAdapter: DataAdapter = DataAdapter()
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private var startClickTime: Long = 0
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
        logi("onDestroy")
        super.onDestroy()
    }

    private fun initView() {
        binding.rvScan.adapter = scanDataAdapter
        binding.rvScan.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        scanDataAdapter.onDataTouchListener = object : OnDataTouchListener {
            override fun onTouch(item: Message, position: Int, view: View, event: MotionEvent) {
                when(event.action){

                    MotionEvent.ACTION_DOWN -> {
                        startClickTime = Calendar.getInstance().timeInMillis
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (item.message.length > 50) {
                            view.parent.requestDisallowInterceptTouchEvent(true)
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        val clickDuration =
                            Calendar.getInstance().timeInMillis - startClickTime
                        if (clickDuration < MAX_CLICK_DURATION && item.recordType == RecordType.Uri) {
                            val browserIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(item.message))
                            startActivity(browserIntent)
                        }
                }}
            }
        }
    }

    private fun initObserver() {
        scanViewModel.ndefMessage.observe(viewLifecycleOwner) { scanDataState ->
            when (scanDataState) {
                is ScanDataState.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is ScanDataState.Success -> {
                    binding.progressBar.isVisible = false
                    binding.tvInit.isVisible = false
                    binding.ivInit.isVisible = false
                    val list = scanDataState.data
                    if (list.isEmpty()) {
                        Toast.makeText(context, "No NDEF Data", Toast.LENGTH_LONG).show()
                    } else {
                        scanDataAdapter.submitList(list)
                    }
                }
                is ScanDataState.Failure -> {
                    val message = scanDataState.throwable.message
                    binding.progressBar.isVisible = false
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun initCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newIntent.collect {
                    scanViewModel.getNdefMessage(it)
                    logi("scanFragmentGetIntent: ${it.hashCode()}")
                }
            }
        }
    }
}
