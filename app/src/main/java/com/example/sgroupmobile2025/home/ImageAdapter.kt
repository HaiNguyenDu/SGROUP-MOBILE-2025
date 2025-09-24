package com.example.sgroupmobile2025.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sgroupmobile2025.databinding.ItemImgBinding
import com.example.sgroupmobile2025.detail.ProductDetailActivity

class ImageAdapter(
    private val context: Context,
    private val products: List<Product>
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemImgBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.imgProduct.setImageResource(product.image)
            binding.txtBrand.text = product.name
            binding.txtName.text = product.description
            binding.txtPrice.text = product.price

            binding.root.setOnClickListener {
                val intent = Intent(context, ProductDetailActivity::class.java)
                intent.putExtra("product", product)
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
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size
}
