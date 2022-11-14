package com.example.nfc_sample_kotlin.view

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.nfc_sample_kotlin.base.BaseFragment
import com.example.nfc_sample_kotlin.R
import com.example.nfc_sample_kotlin.enum.RecordType
import com.example.nfc_sample_kotlin.viewmodel.WriteFragmentViewModel
import com.example.nfc_sample_kotlin.api.ParseNdefMessageImpl.Companion.URI_PREFIX_MAP
import com.example.nfc_sample_kotlin.databinding.AddRecordFragmentBinding
import com.example.nfc_sample_kotlin.hide
import com.example.nfc_sample_kotlin.logi
import com.example.nfc_sample_kotlin.model.Message
import com.example.nfc_sample_kotlin.show
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
        onWriteDataClick()
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
                if (clickItemRecordType == RecordType.Text) {
                    etText.setText(clickItemData)
                    tvTitle.text = textTitle
                    showEditTextRecordUI(this)
                }
                if (clickItemRecordType == RecordType.Uri) {
                    etUri.setText(clickItemData)
                    tvTitle.text = uriTitle
                    showEditUriRecordUI(this)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFragmentResultListener(writeFragmentClickPosition)
        clearFragmentResultListener(writeFragmentClickItem)
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
                if (etUri.visibility == View.INVISIBLE && etText.text.isNotEmpty() && clickDataPosition == noneClickDataPosition) {
                    writeData = etText.text.toString()
                    writeFragmentViewModel.saveWriteData(RecordType.Text, writeData)

                }
                if (etText.visibility == View.INVISIBLE && etUri.text.isNotEmpty() && clickDataPosition == noneClickDataPosition) {
                    writeData = spAddressTitle.selectedItem.toString() + etUri.text.toString()
                    writeFragmentViewModel.saveWriteData(RecordType.Uri, writeData)

                }
                if (etUri.visibility == View.INVISIBLE && etText.text.isNotEmpty() && clickDataPosition != noneClickDataPosition) {
                    writeData = etText.text.toString()
                    writeFragmentViewModel.editItemData(
                        clickDataPosition,
                        RecordType.Text,
                        writeData
                    )
                    logi(writeData)
                }

                if (etText.visibility == View.INVISIBLE && etUri.text.isNotEmpty() && clickDataPosition != noneClickDataPosition) {
                    writeData = spAddressTitle.selectedItem.toString() + etUri.text.toString()
                    writeFragmentViewModel.editItemData(
                        clickDataPosition,
                        RecordType.Uri,
                        writeData
                    )
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

    private fun onWriteDataClick() {
        setFragmentResultListener(writeFragmentClickPosition) { _, bundle ->
            clickDataPosition = bundle.get(writedPosition) as Int
            logi(clickDataPosition.toString())
        }
        setFragmentResultListener(writeFragmentClickItem) { _, bundle ->
            val result: Message = bundle.get(writedItem) as Message
            clickItemRecordType = result.recordType

            if (clickItemRecordType == RecordType.Text) {
                clickItemData = result.message

            }
            if (clickItemRecordType == RecordType.Uri) {
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