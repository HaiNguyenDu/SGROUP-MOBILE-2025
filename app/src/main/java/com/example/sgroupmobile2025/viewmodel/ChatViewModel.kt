package com.example.sgroupmobile2025.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgroupmobile2025.data.model.Message
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(application: Application): ViewModel()  {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val newMsg = Message(text = text, isSent = true)
        _messages.value = _messages.value + newMsg

        simulateBotReply(text)
    }

    private fun simulateBotReply(userText: String) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(800)

            val reply = Message("Nhan tin nhan ne hihi -> $userText", isSent = false)
            _messages.value = _messages.value + reply

            _isLoading.value = false
        }
    }
}
