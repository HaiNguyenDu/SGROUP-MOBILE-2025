package com.example.sgroupmobile2025.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.databinding.ItemSizeBinding

class SizeAdapter(private val sizes: List<String>) :
    RecyclerView.Adapter<SizeAdapter.SizeViewHolder>() {

    private var selectedSize: String = "41"

    inner class SizeViewHolder(val binding: ItemSizeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(size: String) {
            binding.txtSize.text = size

            if (size == selectedSize) {
                binding.txtSize.setBackgroundResource(R.drawable.bg_size_selected) // nền đen
                binding.txtSize.setTextColor(
                    ContextCompat.getColor(binding.root.context, android.R.color.white)
                )
            } else {
                binding.txtSize.setBackgroundResource(R.drawable.bg_size_default) // nền viền xám
                binding.txtSize.setTextColor(
                    ContextCompat.getColor(binding.root.context, android.R.color.black)
                )
            }

            binding.root.setOnClickListener {
                selectedSize = size
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        val binding = ItemSizeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SizeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        holder.bind(sizes[position])
    }

    override fun getItemCount() = sizes.size
}
