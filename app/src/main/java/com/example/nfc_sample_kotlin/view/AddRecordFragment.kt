package com.example.nfc_sample_kotlin.view

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.nfc_sample_kotlin.base.BaseFragment
import com.example.nfc_sample_kotlin.R
import com.example.nfc_sample_kotlin.util.RecordType
import com.example.nfc_sample_kotlin.viewmodel.WriteFragmentViewModel
import com.example.nfc_sample_kotlin.api.ParseNdefMessageImpl.Companion.URI_PREFIX_MAP
import com.example.nfc_sample_kotlin.databinding.AddRecordFragmentBinding
import com.example.nfc_sample_kotlin.hide
import com.example.nfc_sample_kotlin.logi
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.show
import com.example.nfc_sample_kotlin.util.RecordType.Text
import com.example.nfc_sample_kotlin.util.RecordType.Uri
import com.example.nfc_sample_kotlin.viewmodel.WriteDataEvent
import com.example.nfc_sample_kotlin.viewmodel.WriteDataEvent.EditWriteData
import com.example.nfc_sample_kotlin.viewmodel.WriteDataEvent.SaveWriteData
import org.koin.androidx.navigation.koinNavGraphViewModel

private const val textTitle = "Enter your text"
private const val uriTitle = "Enter your URL"
private const val editButtonString = "Edit"
private const val initSpPosition = 1
private const val noneClickDataPosition = -1

class AddRecordFragment :
    BaseFragment<AddRecordFragmentBinding>(AddRecordFragmentBinding::inflate) {

    private val writeFragmentViewModel by koinNavGraphViewModel<WriteFragmentViewModel>(R.id.nav_writefragment)
    private var clickDataPosition: Int = noneClickDataPosition
    private var spPosition = initSpPosition
    private var clickItemData: String? = null
    private var clickItemRecordType: RecordType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logi("onCreate${this.javaClass.hashCode()}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onDataClick()
        initView()
        btnFunction()
        setHideKeyBoard()
        logi("onViewCreated${this.javaClass.hashCode()}")
    }

    override fun onStart() {
        super.onStart()
        logi(clickDataPosition.toString())
    }

    override fun onResume() {
        super.onResume()
        logi(clickDataPosition.toString())
        if (clickItemData != null) {
            binding.apply {
                btOk.text = editButtonString
                if (clickItemRecordType == Text) {
                    etText.setText(clickItemData)
                    tvTitle.text = textTitle
                    showEditTextRecordUI(this)
                }
                if (clickItemRecordType == Uri) {
                    etUri.setText(clickItemData)
                    tvTitle.text = uriTitle
                    showEditUriRecordUI(this)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFragmentResultListener(WRITE_FRAGMENT_CLICK_POSITION)
        clearFragmentResultListener(WRITE_FRAGMENT_CLICK_ITEM)
        logi("onDestroy${this.javaClass.hashCode()}")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.ndef_format_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.format_text -> {
                binding.apply {
                    tvTitle.text = textTitle
                    showEditTextRecordUI(this)
                }
                true
            }
            R.id.format_uri -> {
                binding.apply {
                    tvTitle.text = uriTitle
                    showEditUriRecordUI(this)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() {
        setHasOptionsMenu(true)
        binding.apply {
            spAddressTitle.adapter = context?.let {
                ArrayAdapter(
                    it,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    URI_PREFIX_MAP
                )
            }
            if (clickItemRecordType == null) {
                initEditUI(this)
            }
            spAddressTitle.setSelection(spPosition)
        }
    }

    private fun btnFunction() {
        binding.apply {
            btOk.setOnClickListener {
                var writeData: String
                if (etUri.isInvisible && etText.text.isNotEmpty() && clickDataPosition == noneClickDataPosition) {
                    writeData = etText.text.toString()
                    writeFragmentViewModel.onEvent(SaveWriteData(Text, writeData))
                }

                if (etText.isInvisible && etUri.text.isNotEmpty() && clickDataPosition == noneClickDataPosition) {
                    writeData = spAddressTitle.selectedItem.toString() + etUri.text.toString()
                    writeFragmentViewModel.onEvent(SaveWriteData(Uri, writeData))
                }

                if (etUri.isInvisible && etText.text.isNotEmpty() && clickDataPosition != noneClickDataPosition) {
                    writeData = etText.text.toString()
                    writeFragmentViewModel.onEvent(EditWriteData(clickDataPosition, Text, writeData))
                }

                if (etText.isInvisible && etUri.text.isNotEmpty() && clickDataPosition != noneClickDataPosition) {
                    writeData = spAddressTitle.selectedItem.toString() + etUri.text.toString()
                    writeFragmentViewModel.onEvent(EditWriteData(clickDataPosition, Text, writeData))
                }

                if (etUri.text.isEmpty() && etText.isInvisible) {
                    Toast.makeText(context, "The URI data is empty.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                if (etText.text.isEmpty() && etUri.isInvisible) {
                    Toast.makeText(context, "The text data is empty.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                findNavController().navigate(R.id.action_addRecordFragment_to_writeFragment)
            }
            btCancel.setOnClickListener {
                findNavController().navigate(R.id.action_addRecordFragment_to_writeFragment)
            }
        }
    }

    private fun setHideKeyBoard() {

        fun hideKeyBoard(view: View) {
            val inputMethodManager =
                activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
        binding.etUri.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyBoard(v)
            }
        }
        binding.etText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyBoard(v)
            }
        }
    }

    private fun onDataClick() {
        setFragmentResultListener(WRITE_FRAGMENT_CLICK_POSITION) { _, bundle ->
            clickDataPosition = bundle.get(WRITTEN_POSITION) as Int
            logi(clickDataPosition.toString())
        }
        setFragmentResultListener(WRITE_FRAGMENT_CLICK_ITEM) { _, bundle ->
            val result: Message = bundle.get(WRITTEN_ITEM) as Message
            clickItemRecordType = result.recordType

            if (clickItemRecordType == Text) {
                clickItemData = result.message
            }
            if (clickItemRecordType == Uri) {
                for (i in 1 until URI_PREFIX_MAP.size) {
                    if (result.message.startsWith(URI_PREFIX_MAP[i])) {
                        spPosition = i
                        clickItemData = result.message.substring(URI_PREFIX_MAP[i].length)
                        break
                    }
                }
            }
            logi(result.message)
        }
    }

    private fun showEditTextRecordUI(binding: AddRecordFragmentBinding) {
        binding.etText.show()
        binding.etUri.hide()
        binding.spAddressTitle.hide()
    }

    private fun showEditUriRecordUI(binding: AddRecordFragmentBinding) {
        binding.etText.hide()
        binding.etUri.show()
        binding.spAddressTitle.show()
    }

    private fun initEditUI(binding: AddRecordFragmentBinding) {
        binding.etText.hide()
        binding.etUri.hide()
        binding.spAddressTitle.hide()
    }

}