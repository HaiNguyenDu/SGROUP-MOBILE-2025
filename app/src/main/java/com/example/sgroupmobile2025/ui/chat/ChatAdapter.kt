package com.example.sgroupmobile2025.ui.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sgroupmobile2025.data.model.Message
import com.example.sgroupmobile2025.databinding.MessageLeftBinding
import com.example.sgroupmobile2025.databinding.MessageRightBinding

class ChatAdapter(private var list: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val typeLeft = 0
    private val typeRight = 1

    @SuppressLint("NotifyDataSetChanged")
    fun setMessages(newList: List<Message>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].isSent) typeRight else typeLeft
    }

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == typeRight) {
            val binding = MessageRightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            RightVH(binding)
        } else {
            val binding = MessageLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LeftVH(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = list[position]
        if (holder is RightVH) holder.tv.text = msg.text
        if (holder is LeftVH) holder.tv.text = msg.text
    }

    class LeftVH(private val binding: MessageLeftBinding) : RecyclerView.ViewHolder(binding.root) {
        var tv: TextView = binding.tvMessage
    }

    class RightVH(private val binding: MessageRightBinding) : RecyclerView.ViewHolder(binding.root) {
        var tv: TextView = binding.tvMessage
    }

}
