package com.example.sgroupmobile2025.detail

import GalleryAdapter
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sgroupmobile2025.databinding.ActivityProductDetailBinding
import com.example.sgroupmobile2025.home.Product
import java.text.DecimalFormat

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding

    private var basePrice: Double = 0.0
    private var currentPrice: Double = 0.0
    private var quantity: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product = intent.getSerializableExtra("product") as? Product
        product?.let { bindProduct(it) }
    }

    private fun bindProduct(product: Product) {
        binding.imgProduct.setImageResource(product.image)

        binding.txtName.text = product.name

        val priceNumber = product.price.filter { it.isDigit() || it == '.' }
        basePrice = priceNumber.toDoubleOrNull() ?: 0.0
        currentPrice = basePrice

        binding.txtPrice.text = formatPrice(currentPrice)

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
        val sizeAdapter = SizeAdapter(product.sizes)
        binding.recyclerSizes.adapter = sizeAdapter

        binding.btnBack.setOnClickListener { finish() }

        val position = intent.getIntExtra("position", -1)
        var isFavorite = position % 2 != 0
        binding.btnFavorite.setColorFilter(
            ContextCompat.getColor(
                this,
                if (isFavorite) android.R.color.holo_red_dark else android.R.color.darker_gray
            )
        )
        binding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            binding.btnFavorite.setColorFilter(
                ContextCompat.getColor(
                    this,
                    if (isFavorite) android.R.color.holo_red_dark else android.R.color.darker_gray
                )
            )
        }

        binding.btnPlus.setOnClickListener {
            quantity++
            updatePrice()
        }

        binding.btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updatePrice()
            }
        }
    }

    private fun updatePrice() {
        currentPrice = basePrice * quantity
        binding.txtPrice.text = formatPrice(currentPrice)
    }

    private fun formatPrice(price: Double): String {
        val formatter = DecimalFormat("#,###.##")
        return "${formatter.format(price)} $"
    }
}
