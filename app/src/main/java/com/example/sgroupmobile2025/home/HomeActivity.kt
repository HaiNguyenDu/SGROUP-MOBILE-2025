package com.example.sgroupmobile2025.home

import ProductItem
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sgroupmobile2025.ProductAdapter
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecycler()
    }

    private fun setupRecycler() {
        val items = listOf(
            ProductItem(
                R.drawable.product_1,
                "Nike",
                "Air Force 1 Jester XX Black Sonic Yellow ...",
                "$96"
            ),
            ProductItem(
                R.drawable.product_2,
                "Converse",
                "Run Start Hike Three Color Unisex",
                "$85.5"
            ),
            ProductItem(
                R.drawable.product_3,
                "Nike",
                "Air Jordan 1 Retro High Obsidian UNC",
                "$196"
            ),
            ProductItem(
                R.drawable.product_4,
                "Nike",
                "Air Force 1 Shadow Beige Pale Ivory",
                "$115"
            ),
            ProductItem(
                R.drawable.product_1,
                "Nike",
                "Air Force 1 Jester XX Black Sonic Yellow ...",
                "$96"
            ),
            ProductItem(
                R.drawable.product_2,
                "Converse",
                "Run Start Hike Three Color Unisex",
                "$85.5"
            ),
            ProductItem(
                R.drawable.product_3,
                "Nike",
                "Air Jordan 1 Retro High Obsidian UNC",
                "$196"
            ),
            ProductItem(
                R.drawable.product_4,
                "Nike",
                "Air Force 1 Shadow Beige Pale Ivory",
                "$115"
            ),
            ProductItem(
                R.drawable.product_1,
                "Nike",
                "Air Force 1 Jester XX Black Sonic Yellow ...",
                "$96"
            ),
            ProductItem(
                R.drawable.product_2,
                "Converse",
                "Run Start Hike Three Color Unisex",
                "$85.5"
            ),
            ProductItem(
                R.drawable.product_3,
                "Nike",
                "Air Jordan 1 Retro High Obsidian UNC",
                "$196"
            ),
            ProductItem(
                R.drawable.product_4,
                "Nike",
                "Air Force 1 Shadow Beige Pale Ivory",
                "$115"
            ),
            ProductItem(R.drawable.product_5, "None", "None", "$1000"),
            ProductItem(R.drawable.product_6, "None", "None", "$1000")
        )

        binding.recycleView.layoutManager = GridLayoutManager(this, 2)
        binding.recycleView.adapter = ProductAdapter(items)
    }
}