package com.example.canvas.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.canvas.data.PenItem
import com.example.canvas.databinding.ItemPenBinding
import androidx.core.graphics.toColorInt

class PenAdapter(
    private val items: List<PenItem>,
    private val onPenClick: (PenItem, Int) -> Unit
) : RecyclerView.Adapter<PenAdapter.PenViewHolder>() {
    private var selectedPos = if(items.isNotEmpty()) 0 else RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenViewHolder {
        val binding = ItemPenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PenViewHolder(binding)
    }

    override fun getItemCount() = items.size
    inner class PenViewHolder(val binding: ItemPenBinding) : RecyclerView.ViewHolder(binding.root) {
        val img: ImageView = binding.imgPen
        fun onHolder(position: Int){
            val isSelected = position == selectedPos
            img.setImageResource(items[position].iconRes)
            binding.imgPen.animate()
                .translationY(if(isSelected) -20f else 0f)
                .scaleX(if (isSelected) 1.4f else 1f)
                .scaleY(if (isSelected) 1.4f else 1f)
                .setDuration(250)
                .start()
            itemView.setOnClickListener {
                val old = selectedPos
                selectedPos = position
                if (old != RecyclerView.NO_POSITION && old != selectedPos) {
                    notifyItemChanged(old)
                    notifyItemChanged(position)
                }
                onPenClick(items[position], position)
            }
        }
    }

    override fun onBindViewHolder(holder: PenViewHolder, position: Int) {
        holder.onHolder(position)
    }
}
