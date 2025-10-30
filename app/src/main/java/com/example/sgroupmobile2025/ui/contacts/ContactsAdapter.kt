package com.example.sgroupmobile2025.ui.contacts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.data.model.Contacts
import com.example.sgroupmobile2025.databinding.ContactItemBinding

class ContactsAdapter(private var contacts: List<Contacts>): RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        if(position == contacts.size - 1){
            val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_up_fade_in)
            holder.itemView.startAnimation(animation)
        }
        holder.onHolder(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Contacts>){
        contacts = newList
        this.notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ContactItemBinding): RecyclerView.ViewHolder(binding.root){
        fun onHolder(contact: Contacts){
            binding.tvName.text = contact.name
            binding.tvPhoneNumber.text = contact.phone
            binding.avatar.text = getFirstCharacter(contact.name)
        }
        fun getFirstCharacter(useName: String): String {
            return if(useName.isEmpty()) return " " else useName[0].uppercaseChar().toString()
        }
    }
}