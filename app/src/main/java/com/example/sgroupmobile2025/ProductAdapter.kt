package com.example.sgroupmobile2025

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.databinding.ActivityProductBinding
import com.example.sgroupmobile2025.databinding.ProductItemBinding

class ProductAdapter(private val productList: List<DataProduct>): RecyclerView.Adapter<ProductAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.onHolder(productList[position])
    }

    override fun getItemCount(): Int = productList.size

    inner class ViewHolder(val binding: ProductItemBinding): RecyclerView.ViewHolder(binding.root){
        fun onHolder(productData: DataProduct){
            Glide.with(binding.root.context)
                .load(productData.getImgSrc())
                .into(binding.ivProduct)

            binding.tvProductName.text = productData.getName()
            binding.tvProductDes.text = productData.getDescription()
            binding.tvPrice.text = "$${productData.getPrice()}"
        }
    }
}