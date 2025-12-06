package com.example.sgroupmobile2025.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.databinding.ActivityChatBinding
import com.example.sgroupmobile2025.databinding.ActivityMain2Binding
import com.example.sgroupmobile2025.ui.fragment.adapter.MainPageAdapter
import com.example.sgroupmobile2025.ui.fragment.adapter.MainPageAdapter.Companion.FRAGMENT_ADD_IMAGE
import com.example.sgroupmobile2025.ui.fragment.adapter.MainPageAdapter.Companion.FRAGMENT_MAIN
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import kotlin.getValue

class MainFragmentActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }
    private val viewModel: FragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleToolbar()
        initView()
        observeViewModel()
    }
    fun handleToolbar(){
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initView() {
        binding.viewPage.adapter = MainPageAdapter(this)
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPage
        ) { tab, position ->
            when (position) {
                FRAGMENT_MAIN -> {
                    tab.text = "Main"
                    tab.icon = getDrawable(R.drawable.ic_home)
                }

                FRAGMENT_ADD_IMAGE -> {
                    tab.text = "Add Image"
                    tab.icon = getDrawable(R.drawable.ic_add)
                }
            }
        }.attach()
    }
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.currentItem.collect {
                if(it){
                    binding.viewPage.setCurrentItem(0,true)                }
            }
        }
    }
}