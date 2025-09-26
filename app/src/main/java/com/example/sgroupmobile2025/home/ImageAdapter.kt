package com.example.sgroupmobile2025.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sgroupmobile2025.databinding.ItemImgBinding
import com.example.sgroupmobile2025.detail.ProductDetailActivity

class ImageAdapter(
    private val context: Context,
    private val products: List<Product>
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private val likedStates = MutableList(products.size) { index ->
        // nếu index chẵn thì true (đỏ), lẻ thì false (xám)
        index % 2 == 0
    }

    inner class ViewHolder(val binding: ItemImgBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product, position: Int) {
            binding.imgProduct.setImageResource(product.image)
            binding.txtBrand.text = product.name
            binding.txtName.text = product.shortDescription
            binding.txtPrice.text = product.price

//             Gán màu tim nè ní
            val isLiked = likedStates[position]
            if (isLiked) {
                binding.imgHeart.setColorFilter(
                    ContextCompat.getColor(context, android.R.color.darker_gray)
                )
            } else {
                binding.imgHeart.setColorFilter(
                    ContextCompat.getColor(context, android.R.color.holo_red_dark)
                )
            }

            binding.imgHeart.setOnClickListener {
                likedStates[position] = !likedStates[position]
                notifyItemChanged(position)
            }

            // Chuyển sang  detail
            binding.root.setOnClickListener {
                val intent = Intent(context, ProductDetailActivity::class.java)
                intent.putExtra("product", product)
                intent.putExtra("position", position)
                context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product, position)
    }

    override fun getItemCount(): Int = products.size
}

