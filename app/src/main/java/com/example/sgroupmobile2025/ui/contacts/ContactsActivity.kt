package com.example.sgroupmobile2025.ui.contacts

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.data.model.Contacts
import com.example.sgroupmobile2025.databinding.ActivityContactsBinding
import com.example.sgroupmobile2025.ui.contacts.viewmodel.ContactsViewModel
import kotlinx.coroutines.launch

class ContactsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityContactsBinding.inflate(layoutInflater) }
    private lateinit var contactsViewModel: ContactsViewModel
    private lateinit var adapter: ContactsAdapter
    val contactsList = Contacts.listContacts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        handleViewCompat()
        initUI()
        handleObserve()
        handleToolbar()
    }
    fun initUI(){
        contactsViewModel = ContactsViewModel(application)
        adapter = ContactsAdapter(emptyList())
        binding.rcvContacts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvContacts.adapter = adapter
    }
    fun handleObserve(){
        lifecycleScope.launch {
            contactsViewModel.contacts.collect { contacts ->
                adapter.updateList(contacts)
            }
        }
        lifecycleScope.launch {
            contactsViewModel.isLoading.collect { isLoading ->
                binding.lottieLoading.visibility =  if(isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    fun handleToolbar(){
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contacts, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.ic_add ->{
                addNewContact()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun addNewContact(){
        val newContact = contactsList.random()
        contactsViewModel.addToList(newContact)
    }
    fun handleViewCompat(){
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}