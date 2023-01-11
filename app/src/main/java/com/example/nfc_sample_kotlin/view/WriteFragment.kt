package com.example.nfc_sample_kotlin.view

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nfc_sample_kotlin.*
import com.example.nfc_sample_kotlin.base.BaseFragment
import com.example.nfc_sample_kotlin.view.state.WriteDataState
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.view.adapter.DataAdapter
import com.example.nfc_sample_kotlin.view.adapter.OnDataTouchListener
import com.example.nfc_sample_kotlin.viewmodel.ActivityViewModel
import com.example.nfc_sample_kotlin.viewmodel.WriteFragmentViewModel
import com.example.nfc_sample_kotlin.databinding.FragmentWriteBinding
import com.example.nfc_sample_kotlin.viewmodel.WriteDataEvent.*
import kotlinx.coroutines.launch
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*


const val WRITE_FRAGMENT_CLICK_ITEM = "WriteFragmentClickItem"
const val WRITE_FRAGMENT_CLICK_POSITION = "WriteFragmentClickPosition"
const val WRITTEN_ITEM = "Item"
const val WRITTEN_POSITION = "Position"
const val MAX_CLICK_DURATION = 500

class WriteFragment : BaseFragment<FragmentWriteBinding>(FragmentWriteBinding::inflate) {

    private val viewModel by sharedViewModel<ActivityViewModel>()
    private val writeFragmentViewModel by koinNavGraphViewModel<WriteFragmentViewModel>(R.id.nav_writefragment)
    private lateinit var writeDataAdapter: DataAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper
    private var connectTagEnable = false
    private var builder: AlertDialog.Builder? = null
    private var dialog: AlertDialog? = null
    private var writeDataList: List<Message> = mutableListOf()
    private var startClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logi("onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBtn()
        initView()
        initCollect()
        logi("onViewCreated")
    }

    override fun onResume() {
        super.onResume()
        initObserve()
    }

    override fun onDestroy() {
        super.onDestroy()
        logi("onDestroy")
    }

    private fun initView() {
        writeDataAdapter = DataAdapter()
        binding.rvWrite.adapter = writeDataAdapter
        binding.rvWrite.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        setDataTouchListener()
        setItemTouchHelper()
    }

    private fun initCollect() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newIntent.collect {
                    if (connectTagEnable) {
                        logi("writeFragmentGetIntent: ${it.hashCode()}")
                        writeFragmentViewModel.onEvent(WriteSavedData(it))
                    }
                    if (!connectTagEnable || writeDataList.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Should press button \"WRITE NFC TAG\" before approach an NFC tag",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                writeFragmentViewModel.writeNdefResult.collect {
                    logi("$it")
                    when (it) {
                        WriteDataState.WriteSuccess -> setAlertDialog(
                            "Write Nfc Tag Succeed",
                            "OK",
                            false
                        )
                        WriteDataState.WriteFail -> setAlertDialog(
                            "Write Nfc Tag failed",
                            "ok",
                            false
                        )
                        WriteDataState.TagReadOnly -> setAlertDialog(
                            "Tag is read only",
                            "ok",
                            false
                        )
                        WriteDataState.OverSize -> setAlertDialog(
                            "NDEF message over size",
                            "ok",
                            false
                        )
                        WriteDataState.NullRecord -> setAlertDialog(
                            "NDEF Record is null",
                            "ok",
                            false
                        )
                        WriteDataState.ConnectFail -> setAlertDialog(
                            "Connect NFC tag failed",
                            "ok",
                            false
                        )
                        WriteDataState.WrongFormat -> setAlertDialog(
                            "Not NDEF message format ",
                            "ok",
                            false
                        )
                        WriteDataState.GetTagFail -> setAlertDialog(
                            "Tag is not NDEF type",
                            "ok",
                            false
                        )
                    }
                }
            }
        }
    }

    private fun initBtn() {
        binding.btAdd.setOnClickListener {
            findNavController().navigate(R.id.action_writeFragment_to_addRecordFragment)
        }

        binding.btWrite.setOnClickListener {
            if (writeDataList.isNotEmpty()) {
                setAlertDialog("Approach an Nfc Tag", "Cancel", false)
                connectTagEnable = true
            } else {
                Toast.makeText(context, "Please add a NDEF record", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initObserve() {
        writeFragmentViewModel.writeData.observe(viewLifecycleOwner) {
            writeDataList = it
            writeDataAdapter.submitList(it)
        }
    }

    private fun setItemTouchHelper(){
        itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),
            ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val startPosition = viewHolder.adapterPosition
                val endPosition = target.adapterPosition
                writeFragmentViewModel.onEvent(MoveWriteData(startPosition, endPosition))
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val index = viewHolder.adapterPosition
                writeFragmentViewModel.onEvent(DeleteWriteData(index))
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvWrite)
    }

    private fun setDataTouchListener(){
        writeDataAdapter.onDataTouchListener = object : OnDataTouchListener {
            override fun onTouch(item: Message, position: Int, view: View, event: MotionEvent) {
                when (event.action) {
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
                        if (clickDuration < MAX_CLICK_DURATION) {
                            setFragmentResult(WRITE_FRAGMENT_CLICK_ITEM, bundleOf(WRITTEN_ITEM to item))
                            setFragmentResult(
                                WRITE_FRAGMENT_CLICK_POSITION,
                                bundleOf(WRITTEN_POSITION to position)
                            )
                            findNavController().navigate(R.id.action_writeFragment_to_addRecordFragment)
                        }
                    }
                }
            }
        }
    }

    private fun setAlertDialog(title: String, button: String, status: Boolean) {
        if (connectTagEnable) {
            dialog?.cancel()
        }
        builder = AlertDialog.Builder(requireContext())
        builder?.apply {
            setTitle(title)
            setPositiveButton(
                button
            ) { dialog, _ ->
                connectTagEnable = status
                dialog?.cancel()
            }
        }
        dialog = builder?.create()
        dialog?.apply {
            setCanceledOnTouchOutside(false)
            show()
        }
    }
}