package com.example.nfc_sample_kotlin.view

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.nfc_sample_kotlin.view.base.BaseFragment
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
import com.example.nfc_sample_kotlin.viewmodel.WriteDataEvent.EditWriteData
import com.example.nfc_sample_kotlin.viewmodel.WriteDataEvent.SaveWriteData
import org.koin.androidx.navigation.koinNavGraphViewModel

private const val TEXT_TITLE = "Enter your text"
private const val URI_TITLE = "Enter your URL"
private const val EDIT_BUTTON_STRING = "Edit"
private const val INIT_SP_POSITION = 1
private const val NONE_CLICK_DATA_POSITION = -1

class AddRecordFragment :
    BaseFragment<AddRecordFragmentBinding>(AddRecordFragmentBinding::inflate) {

    private val writeFragmentViewModel by koinNavGraphViewModel<WriteFragmentViewModel>(R.id.nav_writefragment)
    private var clickDataPosition: Int = NONE_CLICK_DATA_POSITION
    private var spPosition = INIT_SP_POSITION
    private var clickItemData: String? = null
    private var itemRecordType: RecordType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logi("onCreate${this.javaClass.hashCode()}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickedDateCheck()
        initView()
        btnFunction()
        setHideKeyBoard()
        logi("onViewCreated${this.javaClass.hashCode()}")
    }

    override fun onResume() {
        super.onResume()
        logi(clickDataPosition.toString())
        if (clickItemData != null) {
            showClickedData()
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
                itemRecordType = Text
                showRecordUI(null, null)
                true
            }
            R.id.format_uri -> {
                itemRecordType = Uri
                showRecordUI(null, null)
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
            if (itemRecordType == null) {
                initEditUI(this)
            }
            spAddressTitle.setSelection(spPosition)
        }
    }

    private fun showClickedData() {
        showRecordUI(clickItemData, EDIT_BUTTON_STRING)

    }

    private fun btnFunction() {
        binding.apply {
            btOk.setOnClickListener {
                var writeData: String
                if (itemRecordType == Text) {
                    if (etText.text.isEmpty()) {
                        Toast.makeText(context, "The Text data is empty.", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    } else {
                        writeData = etText.text.toString()
                        editDataToViewModel(writeData)
                    }
                }
                if (itemRecordType == Uri) {
                    if (etUri.text.isEmpty()) {
                        Toast.makeText(context, "The Uri data is empty.", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    } else {
                        writeData = spAddressTitle.selectedItem.toString() + etUri.text.toString()
                        editDataToViewModel(writeData)
                    }
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

    private fun editDataToViewModel(writeData: String) {
        if (clickDataPosition == NONE_CLICK_DATA_POSITION) {
            writeFragmentViewModel.onEvent(SaveWriteData(itemRecordType!!, writeData))
        }
        if (clickDataPosition != NONE_CLICK_DATA_POSITION) {
            writeFragmentViewModel.onEvent(
                EditWriteData(
                    clickDataPosition,
                    itemRecordType!!,
                    writeData
                )
            )
        }
    }

    private fun onClickedDateCheck() {
        setFragmentResultListener(WRITE_FRAGMENT_CLICK_POSITION) { _, bundle ->
            clickDataPosition = bundle.get(WRITTEN_POSITION) as Int
            logi(clickDataPosition.toString())
        }
        setFragmentResultListener(WRITE_FRAGMENT_CLICK_ITEM) { _, bundle ->
            val result: Message = bundle.get(WRITTEN_ITEM) as Message
            itemRecordType = result.recordType

            if (itemRecordType == Text) {
                clickItemData = result.message
            }
            if (itemRecordType == Uri) {
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

    private fun showRecordUI(textContent: String?, btTitle: String?) {
        binding.apply {

            btTitle?.let {
                btOk.text = btTitle
            }

            if (itemRecordType == Text) {
                textContent?.let {
                    etText.setText(textContent)
                }
                tvTitle.text = TEXT_TITLE
                etText.show()
                etUri.hide()
                spAddressTitle.hide()
            }
            if (itemRecordType == Uri) {
                textContent?.let {
                    etUri.setText(textContent)
                }
                spAddressTitle.setSelection(spPosition)
                tvTitle.text = URI_TITLE
                binding.etText.hide()
                binding.etUri.show()
                binding.spAddressTitle.show()
            }
        }
    }

    private fun initEditUI(binding: AddRecordFragmentBinding) {
        binding.etText.hide()
        binding.etUri.hide()
        binding.spAddressTitle.hide()
    }
}