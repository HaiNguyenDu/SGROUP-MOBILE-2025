package com.example.sgroupmobile2025.ui.fragment.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgroupmobile2025.databinding.ImageFragmentItemBinding
import com.example.sgroupmobile2025.databinding.ImageItemBinding
import com.example.sgroupmobile2025.ui.fragment.model.Poster

class MainFragmentAdater(var posters: List<Poster>): RecyclerView.Adapter<MainFragmentAdater.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ImageFragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.onHolder(position)
    }

    override fun getItemCount(): Int = posters.size + 1

    @SuppressLint("NotifyDataSetChanged")
    fun updateImages(newPosters: List<Poster>){
        posters = newPosters
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ImageFragmentItemBinding): RecyclerView.ViewHolder(binding.root){
        fun onHolder(position: Int){
            if(position == 0){
                binding.rcv.visibility = View.GONE
                binding.tvName.text = "Poster Fragment"
                binding.tvName.gravity = Gravity.CENTER
            }
            else {
                binding.tvName.text = posters[position - 1].title
                binding.rcv.layoutManager =
                    LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                binding.rcv.adapter = ListImageAdapter(Poster.listPosters[position - 1].images)
            }
        }
    }
}