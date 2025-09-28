package com.example.sgroupmobile2025

import ProductItem
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.ui.layout.Layout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.databinding.ItemProductBinding
import com.example.sgroupmobile2025.detail.DetailActivity

class ProductAdapter(
    private val items: List<ProductItem>,
    private val onClick: (ProductItem, Int) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductItem, position: Int) {
            binding.ivProduct.setImageResource(item.imageRes)
            binding.tvBrand.text = item.brand
            binding.tvDescription.text = item.description
            binding.tvCost.text = item.price
            setHeartIcon(item.isFavorite)
            binding.ivHeart.setOnClickListener {
                item.isFavorite = !item.isFavorite
                setHeartIcon(item.isFavorite)
            }
            binding.root.setOnClickListener {
                onClick(item, position)
            }
        }
        private fun setHeartIcon(isFavorite: Boolean) {
            if (isFavorite) {
                binding.ivHeart.setColorFilter(
                    ContextCompat.getColor(binding.root.context, R.color.red)
                )
            } else {
                binding.ivHeart.setColorFilter(
                    ContextCompat.getColor(binding.root.context, R.color.black)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount() = items.size
}