package com.example.sgroupmobile2025.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.sgroupmobile2025.databinding.FragmentAddBinding
import com.example.sgroupmobile2025.databinding.DialogCustomBinding
import com.example.sgroupmobile2025.ui.fragment.model.Poster
import kotlinx.coroutines.launch
import kotlin.getValue
import android.app.Dialog

class AddFragment : Fragment() {

    private val binding by lazy { FragmentAddBinding.inflate(layoutInflater) }
    private val viewModel: FragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setOnClick()
        observeDialog()
    }
    private fun initView() {
        val titles = Poster.listPosters.map { it.title }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            titles
        )

        binding.spinnerDefault.adapter = adapter
    }
    fun setOnClick(){
        binding.btnAdd.setOnClickListener {
            val selectedTitle = binding.spinnerDefault.selectedItem.toString()
            val url = binding.edtUrl.text.toString()

            if(url.isBlank()){
                Toast.makeText(requireContext(), "Nhap URL!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.addPoster(selectedTitle, url)
            viewModel.setCurrentItem()

            lifecycleScope.launch {
                viewModel.triggerDialog("Thêm poster thành công")
            }
        }

    }

    private fun observeDialog() {
        lifecycleScope.launch {
            viewModel.showDialog.collect { message ->
                showCustomDialog(message)
            }
        }
    }

    private fun showCustomDialog(message: String) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogCustomBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(dialogBinding.root)

        dialog.window?.apply {
            setLayout(
                (resources.displayMetrics.widthPixels * 0.75).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }

        dialogBinding.tvMessage.text = message

        dialogBinding.btnDialogConfirm.setOnClickListener {
            binding.edtUrl.text.clear()
            dialog.dismiss()
        }

        dialogBinding.btnDialogCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
