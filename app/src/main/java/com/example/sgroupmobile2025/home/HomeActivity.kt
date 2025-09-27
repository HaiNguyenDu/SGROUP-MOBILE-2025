package com.example.sgroupmobile2025.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupUi()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupUi() {
        val listProduct = listOf(
            Product(
                name = "Nike",
                price = "96$",
                image = R.drawable.nike,
                shortDescription = "Air Force 1 Jester XX Black Sonic Yellow...",
                longDescription = "These aren't just shoes; they're a statement of effortless style. The classic white sneakers are crafted from supple, high-quality leather that feels soft and supportive from the moment you slip them on. Their design is wonderfully minimalist, featuring clean lines and only the slightest hint of texture near the heel. They possess a remarkable versatility, ...",
                gallery = listOf(R.drawable.detail_jodan_1, R.drawable.air1, R.drawable.balen, R.drawable.balen1, R.drawable.nikecolor, R.drawable.converse, R.drawable.nike, R.drawable.adidas),
                sizes = listOf("37", "38", "39","40","41","42","43","44")
            ),
            Product(
                name = "Balen Track",
                price = "85.5$",
                image = R.drawable.balen,
                shortDescription = "Run Star Hike Three Color Unisex",
                longDescription = "These aren't just shoes; they're a statement of effortless style. The classic white sneakers are crafted from supple, high-quality leather that feels soft and supportive from the moment you slip them on. Their design is wonderfully minimalist, featuring clean lines and only the slightest hint of texture near the heel. They possess a remarkable versatility, ...",
                gallery = listOf(R.drawable.detail_balen1, R.drawable.detail_balen11, R.drawable.detail_balen_12, R.drawable.detail_balen_13, R.drawable.detail_balen_12, R.drawable.detail_balen_13),
                sizes = listOf("37", "38", "39","40","41","42","43","44")
            ),
            Product(
                name = "Nike",
                price = "196$",
                image = R.drawable.nikecolor,
                shortDescription = "Air Jordan 1 Retro High Obsidian UNC",
                longDescription = "These aren't just shoes; they're a statement of effortless style. The classic white sneakers are crafted from supple, high-quality leather that feels soft and supportive from the moment you slip them on. Their design is wonderfully minimalist, featuring clean lines and only the slightest hint of texture near the heel. They possess a remarkable versatility, ...",
                gallery = listOf(R.drawable.nike, R.drawable.air1, R.drawable.balen, R.drawable.balen1, R.drawable.nikecolor, R.drawable.converse, R.drawable.nike, R.drawable.adidas),
                sizes = listOf("37", "38", "39","40","41","42","43","44")
            ),
            Product(
                name = "Converse",
                price = "115$",
                image = R.drawable.converse,
                shortDescription = "Air Force 1 Shadow Beige Pale Ivory",
                longDescription = "These aren't just shoes; they're a statement of effortless style. The classic white sneakers are crafted from supple, high-quality leather that feels soft and supportive from the moment you slip them on. Their design is wonderfully minimalist, featuring clean lines and only the slightest hint of texture near the heel. They possess a remarkable versatility, ...",
                gallery = listOf(R.drawable.nike, R.drawable.air1, R.drawable.balen, R.drawable.balen1, R.drawable.nikecolor, R.drawable.converse, R.drawable.nike, R.drawable.adidas),
                sizes = listOf("37", "38", "39","40","41","42","43","44")
            ),
            Product(
                name = "Nike",
                price = "96$",
                image = R.drawable.balen,
                shortDescription = "Air Force 1 Jester XX Black Sonic Yellow...",
                longDescription = "These aren't just shoes; they're a statement of effortless style. The classic white sneakers are crafted from supple, high-quality leather that feels soft and supportive from the moment you slip them on. Their design is wonderfully minimalist, featuring clean lines and only the slightest hint of texture near the heel. They possess a remarkable versatility, ...",
                gallery = listOf(R.drawable.detail_balen1, R.drawable.detail_balen11, R.drawable.detail_balen_12, R.drawable.detail_balen_13, R.drawable.detail_balen_12, R.drawable.detail_balen_13),
                sizes = listOf("37", "38", "39","40","41","42","43","44")
            ),
            Product(
                name = "Converse",
                price = "85.5$",
                image = R.drawable.adidas,
                shortDescription = "Run Star Hike Three Color Unisex",
                longDescription = "These aren't just shoes; they're a statement of effortless style. The classic white sneakers are crafted from supple, high-quality leather that feels soft and supportive from the moment you slip them on. Their design is wonderfully minimalist, featuring clean lines and only the slightest hint of texture near the heel. They possess a remarkable versatility, ...",
                gallery = listOf(R.drawable.nike, R.drawable.air1, R.drawable.balen, R.drawable.balen1, R.drawable.nikecolor, R.drawable.converse, R.drawable.nike, R.drawable.adidas),
                sizes = listOf("37", "38", "39","40","41","42","43","44")
            ),
            Product(
                name = "Nike",
                price = "196$",
                image = R.drawable.nikecolor,
                shortDescription = "Air Jordan 1 Retro High Obsidian UNC",
                longDescription = "These aren't just shoes; they're a statement of effortless style. The classic white sneakers are crafted from supple, high-quality leather that feels soft and supportive from the moment you slip them on. Their design is wonderfully minimalist, featuring clean lines and only the slightest hint of texture near the heel. They possess a remarkable versatility, ...",
                gallery = listOf(R.drawable.nike, R.drawable.air1, R.drawable.balen, R.drawable.balen1, R.drawable.nikecolor, R.drawable.converse, R.drawable.nike, R.drawable.adidas),
                sizes = listOf("37", "38", "39","40","41","42","43","44")
            )
        )

        val adapter = ImageAdapter(this, listProduct)
        binding.recyclerViewProducts.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewProducts.adapter = adapter
    }
}
