package com.example.sgroupmobile2025.ui.gallery

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.data.model.Image
import com.example.sgroupmobile2025.databinding.ImageItemBinding

class LocalImageAdapter(private val images: List<Image>): RecyclerView.Adapter<LocalImageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val imgSrc = images[position].imgSrc
        holder.onHolder(imgSrc)
    }

    override fun getItemCount(): Int = images.size

    class ViewHolder(private val binding: ImageItemBinding): RecyclerView.ViewHolder(binding.root){
        fun onHolder(imageUri: Uri){
            Glide.with(binding.root.context)
                .load(imageUri)
                .into(binding.ivImage)
        }
    }
}