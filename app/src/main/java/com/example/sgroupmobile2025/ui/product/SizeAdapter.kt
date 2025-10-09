package com.example.sgroupmobile2025.ui.product

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sgroupmobile2025.databinding.SizeItemBinding

class SizeAdapter(private val listSize: List<Int>): RecyclerView.Adapter<SizeAdapter.ViewHolder>(){
    private var selectedItem = listSize.indexOf(40)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = SizeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.onHolder(position)
    }

    override fun getItemCount(): Int = listSize.size

    inner class ViewHolder(private val binding: SizeItemBinding): RecyclerView.ViewHolder(binding.root){
        fun onHolder(position: Int){
            val size = listSize[position]
            binding.tvSize.text = size.toString()
            if(position == selectedItem){
                binding.tvSize.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.holo_orange_light)
                )
            }
            else{
                binding.tvSize.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.transparent)
                )
            }
            itemView.setOnClickListener {
                val preSelected = selectedItem
                selectedItem = position
                notifyItemChanged(preSelected)
                notifyItemChanged(selectedItem)
            }
        }
    }
}