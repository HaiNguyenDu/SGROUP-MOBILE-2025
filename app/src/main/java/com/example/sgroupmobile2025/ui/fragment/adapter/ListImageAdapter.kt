package com.example.sgroupmobile2025.ui.fragment.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.databinding.PosterItemBinding

class ListImageAdapter(val images: List<String>): RecyclerView.Adapter<ListImageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = PosterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.onHolder(position)
    }



    override fun getItemCount(): Int = images.size

    inner class ViewHolder(private val binding: PosterItemBinding): RecyclerView.ViewHolder(binding.root){
        fun onHolder(position: Int){
            Glide.with(binding.root)
                .load(images[position])
                .into(binding.ivImage)
        }
    }
}