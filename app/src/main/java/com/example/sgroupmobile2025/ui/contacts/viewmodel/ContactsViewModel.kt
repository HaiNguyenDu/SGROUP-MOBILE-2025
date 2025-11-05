package com.example.sgroupmobile2025.ui.contacts.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgroupmobile2025.data.model.Contacts
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactsViewModel(application: Application): ViewModel() {
    private val _contacts = MutableStateFlow<List<Contacts>>(emptyList())
    private var _isLoad = MutableStateFlow<Boolean>(false)
    val contacts: StateFlow<List<Contacts>> = _contacts
    val isLoading: StateFlow<Boolean> = _isLoad
    fun addToList(contacts: Contacts){
        viewModelScope.launch {
            _isLoad.value = true
            delay(2000)
            _isLoad.value = false
            _contacts.value = _contacts.value + contacts
        }
    }
}