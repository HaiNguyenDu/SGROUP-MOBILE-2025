package com.example.canvas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.canvas.R

class ColorAdapter(
    private val colors: List<Int>,
    private val onColorSelected: (Int) -> Unit
) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    inner class ColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val colorView: View = view.findViewById(R.id.colorView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_color_cell, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = colors[position]
        holder.colorView.setBackgroundColor(color)
        holder.itemView.setOnClickListener { onColorSelected(color) }
    }

    override fun getItemCount() = colors.size
}