package com.example.nfc_sample_kotlin.View

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.nfc_sample_kotlin.Base.BaseFragment
import com.example.nfc_sample_kotlin.R
import com.example.nfc_sample_kotlin.RecordType
import com.example.nfc_sample_kotlin.ViewModel.WriteFragmentViewModel
import com.example.nfc_sample_kotlin.api.ParseNdefMessageImpl
import com.example.nfc_sample_kotlin.databinding.AddRecordFragmentBinding

private const val textTitle = "Enter your text"
private const val uriTitle = "Enter your URL"
private const val initSpPosition = 1

class AddRecordFragment :
    BaseFragment<AddRecordFragmentBinding>(AddRecordFragmentBinding::inflate) {

    private val writeFragmentViewModel : WriteFragmentViewModel by navGraphViewModels(R.id.nav_writefragment)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        btnFunction()

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
                    etText.visibility = View.VISIBLE
                    etUri.visibility = View.INVISIBLE
                    spAddressTitle.visibility = View.INVISIBLE
                }
                true
            }
            R.id.format_uri -> {

                binding.apply {
                    tvTitle.text = uriTitle
                    etText.visibility = View.INVISIBLE
                    etUri.visibility = View.VISIBLE
                    spAddressTitle.visibility = View.VISIBLE
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() {

        setHasOptionsMenu(true)
        binding.apply {
            etText.visibility = View.INVISIBLE
            etUri.visibility = View.INVISIBLE
            spAddressTitle.visibility = View.INVISIBLE
            spAddressTitle.adapter = context?.let {
                ArrayAdapter(
                    it,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    ParseNdefMessageImpl.URI_PREFIX_MAP
                )
            }
            spAddressTitle.setSelection(initSpPosition)
        }
    }

    private fun btnFunction() {

        binding.apply {
            btOk.setOnClickListener {
                var writeData: String
                if (etUri.visibility == View.INVISIBLE && etText.text.isNotEmpty()) {
                    writeData = etText.text.toString()
                    writeFragmentViewModel.saveWriteData(RecordType.Text,writeData)

                }
                if (etText.visibility == View.INVISIBLE && etUri.text.isNotEmpty()) {
                    writeData = spAddressTitle.selectedItem.toString() + etUri.text.toString()
                    writeFragmentViewModel.saveWriteData(RecordType.Uri,writeData)

                }
                findNavController().navigate(R.id.action_addRecordFragment_to_writeFragment)
            }
            btCancel.setOnClickListener {
                findNavController().navigate(R.id.action_addRecordFragment_to_writeFragment)
            }
        }
    }
}