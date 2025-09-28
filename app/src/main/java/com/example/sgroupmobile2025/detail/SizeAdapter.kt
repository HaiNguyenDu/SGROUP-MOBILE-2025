package com.example.sgroupmobile2025.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.databinding.ItemSizeBinding

class SizeAdapter (
    private val sizes: List<SizeItem>,
    private val onClick: (SizeItem) -> Unit
) : RecyclerView.Adapter<SizeAdapter.SizeViewHolder>(){
    inner class SizeViewHolder(val binding: ItemSizeBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SizeItem) {
            binding.tvSizeShoes.text = item.size
            if (item.isSelected) {
                binding.tvSizeShoes.setBackgroundColor(
                    binding.root.context.getColor(R.color.black)
                )
                binding.tvSizeShoes.setTextColor(
                    binding.root.context.getColor(R.color.white)
                )
            }
            else{
                binding.tvSizeShoes.setBackgroundColor(
                    binding.root.context.getColor(R.color.white)
                )
                binding.tvSizeShoes.setTextColor(
                    binding.root.context.getColor(R.color.black)
                )
            }
            binding.root.setOnClickListener {
                onClick(item)
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