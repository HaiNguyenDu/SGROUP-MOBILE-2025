package com.example.sgroupmobile2025.ui.chat

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sgroupmobile2025.databinding.ActivityChatBinding
import com.example.sgroupmobile2025.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private val binding by lazy { ActivityChatBinding.inflate(layoutInflater) }
    private lateinit var adapter: ChatAdapter
    private lateinit var viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        handleViewCompat()
        initView()
        observeViewModel()
        handleToolbar()
        onClick()
    }
    fun initView(){
        adapter = ChatAdapter(emptyList())
        binding.recyclerChat.adapter = adapter
        binding.recyclerChat.layoutManager = LinearLayoutManager(this)
        viewModel = ChatViewModel(application)
    }
    fun handleToolbar(){
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    fun onClick(){
        binding.btnSend.setOnClickListener {

            val text = binding.edtMessage.text.toString().trim()
            viewModel.sendMessage(text)
            binding.edtMessage.setText("")
        }
    }
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.messages.collect { list ->
                adapter.setMessages(list)
                binding.recyclerChat.scrollToPosition(list.size - 1)
            }
        }
    }
    fun handleViewCompat(){
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}