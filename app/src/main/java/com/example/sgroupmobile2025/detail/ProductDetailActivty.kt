package com.example.sgroupmobile2025.detail

import GalleryAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sgroupmobile2025.databinding.ActivityProductDetailBinding
import com.example.sgroupmobile2025.home.Product

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nhận dữ liệu từ Intent
        val product = intent.getSerializableExtra("product") as? Product
        product?.let { bindProduct(it) }
    }

    private fun bindProduct(product: Product) {
        // Ảnh chính
        binding.imgProduct.setImageResource(product.image)

        // Tên & giá
        binding.txtName.text = product.name
        binding.txtPrice.text = product.price

        // Mô tả
        binding.txtDescription.text = product.description

        // Gallery (ảnh nhỏ)
        binding.recyclerGallery.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerGallery.adapter = GalleryAdapter(product.gallery)

        // Size
        binding.recyclerSizes.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerSizes.adapter = SizeAdapter(product.sizes)
    }
}

