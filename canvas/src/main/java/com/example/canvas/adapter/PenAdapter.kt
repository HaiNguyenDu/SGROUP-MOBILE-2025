package com.example.canvas.adapter

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
    }

    fun setSelected(position: Int) {
        val old = selectedPos
        selectedPos = position
        if (old != RecyclerView.NO_POSITION) notifyItemChanged(old)
        notifyItemChanged(position)
    }
    override fun onBindViewHolder(holder: PenViewHolder, position: Int) {
        val item = items[position]
        holder.img.setImageResource(item.iconRes)

        updateViewStatus(holder.itemView, position == selectedPos)

        holder.itemView.setOnClickListener {
            onPenClick(item, position)
        }
    }


    private fun updateViewStatus(view: View, isSelected: Boolean) {
        view.animate()
            .translationY(if (isSelected) -40f else 0f)
            .scaleX(if (isSelected) 1.2f else 1f)
            .scaleY(if (isSelected) 1.2f else 1f)
            .setDuration(200)
            .start()

        val colorFrom = if (isSelected) android.graphics.Color.TRANSPARENT else "#E0E0E0".toColorInt()
        val colorTo = if (isSelected) "#E0E0E0".toColorInt() else android.graphics.Color.TRANSPARENT

        val colorAnimation = android.animation.ValueAnimator.ofObject(android.animation.ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 200
        colorAnimation.addUpdateListener { animator ->
            view.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()
    }
}
