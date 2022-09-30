package com.example.nfc_sample_kotlin.View

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nfc_sample_kotlin.Base.BaseFragment
import com.example.nfc_sample_kotlin.R
import com.example.nfc_sample_kotlin.TAG
import com.example.nfc_sample_kotlin.View.Adapter.DataAdapter
import com.example.nfc_sample_kotlin.View.Adapter.OnDataTouchListener
import com.example.nfc_sample_kotlin.ViewModel.ActivityViewModel
import com.example.nfc_sample_kotlin.ViewModel.WriteFragmentViewModel
import com.example.nfc_sample_kotlin.databinding.FragmentWriteBinding
import com.example.nfc_sample_kotlin.logd
import com.example.nfc_sample_kotlin.logi
import kotlinx.coroutines.launch

class WriteFragment : BaseFragment<FragmentWriteBinding>(FragmentWriteBinding::inflate) {

    private val viewModel: ActivityViewModel by activityViewModels()
    private val writeFragmentViewModel: WriteFragmentViewModel by navGraphViewModels(R.id.nav_writefragment)
    private lateinit var writeDataAdapter: DataAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper
    private var connectTagEnable = false
    private var builder: AlertDialog.Builder? = null
    private var dialog: AlertDialog? = null

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
        writeDataAdapter.onDataTouchListener = object : OnDataTouchListener {
            override fun onTouch(message: String, position: Int, view: View, event: MotionEvent) {
                if (event.action == MotionEvent.ACTION_DOWN && message.length > 100) {
                    view.parent.requestDisallowInterceptTouchEvent(true)

                }
                if (event.action == MotionEvent.ACTION_UP) {
                    logd("onClick: $position")

                }
            }
        }

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
                writeFragmentViewModel.moveWriteData(startPosition, endPosition)
                return true

            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val index = viewHolder.adapterPosition
                writeFragmentViewModel.deleteWriteData(index)

            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvWrite)
    }

    private fun initCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.newIntent.collect {
                    if (connectTagEnable) {
                        logi("writeFragmentGetIntent: ${it.hashCode()}")
                        writeFragmentViewModel.writeSavedData(it)
                    }

                    if (!connectTagEnable || writeDataAdapter.listNdefPayload.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Should press button \"WRITE NFC TAG\" before approach an NFC tag",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                writeFragmentViewModel.writeNdefResult.collect { result ->
                    if (connectTagEnable) {
                        logi("writeNdefResult: ${result.hashCode()}")
                        if (result) {
                            setAlertDialog("Write Nfc Tag Succeed", "OK", false)

                        } else {
                            setAlertDialog("Write Nfc Tag failed", "ok", false)

                        }
                        Log.d(TAG, "initObserve: $result")
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
            if (writeDataAdapter.listNdefPayload.isNotEmpty()) {
                setAlertDialog("Approach an Nfc Tag", "Cancel", false)
                connectTagEnable = true

            } else {
                Toast.makeText(context, "Please add a NDEF record", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun initObserve() {
        writeFragmentViewModel.writeData.observe(viewLifecycleOwner) {
            writeDataAdapter.listNdefPayload = it
            writeDataAdapter.notifyDataSetChanged()

        }
    }

    private fun setAlertDialog(title: String, button: String, status: Boolean) {
        if (connectTagEnable) {
            dialog?.cancel()
        }
        builder = AlertDialog.Builder(requireContext())
        builder?.apply {
            setTitle(title)
            setPositiveButton(button, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    connectTagEnable = status
                    dialog?.cancel()
                }
            })
        }
        dialog = builder?.create()
        dialog?.apply {
            setCanceledOnTouchOutside(false)
            show()
        }
    }
}