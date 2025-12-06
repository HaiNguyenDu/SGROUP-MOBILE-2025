package com.example.sgroupmobile2025.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sgroupmobile2025.R
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.sgroupmobile2025.databinding.FragmentMainBinding
import com.example.sgroupmobile2025.ui.fragment.adapter.MainFragmentAdater
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private val viewModel: FragmentViewModel by activityViewModels()
    private lateinit var mainAdapter: MainFragmentAdater
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        mainAdapter = MainFragmentAdater(emptyList())
        observeData()
    }
    private fun initView() {

        binding.rcv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rcv.adapter = mainAdapter
    }
    private fun observeData() {
        lifecycleScope.launch {
            viewModel.posters.collect { list ->
                mainAdapter.updateImages(list)
            }
        }
    }

}