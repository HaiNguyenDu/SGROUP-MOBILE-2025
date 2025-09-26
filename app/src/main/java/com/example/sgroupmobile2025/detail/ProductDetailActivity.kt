package com.example.sgroupmobile2025.detail

import GalleryAdapter
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
        binding.imgProduct.setImageResource(product.image)

        binding.txtName.text = product.name
        binding.txtPrice.text = product.price


        binding.txtShortDescription.text = product.shortDescription
        binding.txtLongDescription.text = product.longDescription

        var isExpanded = false

        binding.txtReadMore.setOnClickListener {
            if (isExpanded) {
                binding.txtLongDescription.maxLines = 3
                binding.txtLongDescription.ellipsize = TextUtils.TruncateAt.END
                binding.txtReadMore.text = "Read more"
            } else {
                binding.txtLongDescription.maxLines = Int.MAX_VALUE
                binding.txtLongDescription.ellipsize = null
                binding.txtReadMore.text = "Read less"
            }
            isExpanded = !isExpanded
        }






        binding.recyclerGallery.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerGallery.adapter = GalleryAdapter(product.gallery)

        binding.recyclerSizes.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerSizes.adapter = SizeAdapter(product.sizes)

        binding.btnBack.setOnClickListener { finish() }



        binding.recyclerSizes.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

// Truyền danh sách size vào adapter
        val sizeAdapter = SizeAdapter(product.sizes)

// Gán adapter cho RecyclerView
        binding.recyclerSizes.adapter = sizeAdapter



        val position = intent.getIntExtra("position", -1)
        var isFavorite = position % 2 != 0 // chẵn = true, lẻ = false

        if (isFavorite) {
            binding.btnFavorite.setColorFilter(
                ContextCompat.getColor(this, android.R.color.holo_red_dark)
            )
        } else {
            binding.btnFavorite.setColorFilter(
                ContextCompat.getColor(this, android.R.color.darker_gray)
            )
        }

        binding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                binding.btnFavorite.setColorFilter(
                    ContextCompat.getColor(this, android.R.color.holo_red_dark)
                )
            } else {
                binding.btnFavorite.setColorFilter(
                    ContextCompat.getColor(this, android.R.color.darker_gray)
                )
            }
        }
    }
}

