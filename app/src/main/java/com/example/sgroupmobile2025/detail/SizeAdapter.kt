package com.example.sgroupmobile2025.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sgroupmobile2025.databinding.ItemSizeBinding

class SizeAdapter(private val sizes: List<String>) :
    RecyclerView.Adapter<SizeAdapter.SizeViewHolder>() {

    inner class SizeViewHolder(val binding: ItemSizeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        val binding = ItemSizeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SizeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        holder.binding.txtSize.text = sizes[position]
    }

    override fun getItemCount() = sizes.size
}
