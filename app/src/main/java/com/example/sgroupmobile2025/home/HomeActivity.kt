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
                "Nike",
                "96$",
                "Air Force 1 Jester XX Black Sonic Yellow...",
                R.drawable.nike,
                listOf(R.drawable.nike, R.drawable.air1, R.drawable.nikecolor),
                listOf("36", "37", "38", "39","40","41","42","43","44")
            ),
            Product(
                "Converse",
                "85.5$",
                "Run Star Hike Three Color Unisex",
                R.drawable.air1,
                listOf(R.drawable.air1, R.drawable.converse, R.drawable.nike),
                listOf("36", "37", "38", "39","40","41","42","43","44")
            ),
            Product(
                "Nike",
                "196$",
                "Air Jordan 1 Retro High Obsidian UNC",
                R.drawable.nikecolor,
                listOf(R.drawable.nikecolor, R.drawable.nike, R.drawable.air1),
                listOf("36", "37", "38", "39","40","41","42","43","44")
            ),
            Product(
                "Converse",
                "115$",
                "Air Force 1 Shadow Beige Pale Ivory",
                R.drawable.converse,
                listOf(R.drawable.converse, R.drawable.air1, R.drawable.nikecolor),
                listOf("36", "37", "38", "39","40","41","42","43","44")
            ),
            Product(
                "Nike",
                "96$",
                "Air Force 1 Jester XX Black Sonic Yellow...",
                R.drawable.nike,
                listOf(R.drawable.nike, R.drawable.air1, R.drawable.nikecolor),
                listOf("36", "37", "38", "39","40","41","42","43","44")
            ),
            Product(
                "Converse",
                "85.5$",
                "Run Star Hike Three Color Unisex",
                R.drawable.air1,
                listOf(R.drawable.air1, R.drawable.converse, R.drawable.nike),
                listOf("36", "37", "38", "39","40","41","42","43","44")
            ),
            Product(
                "Nike",
                "196$",
                "Air Jordan 1 Retro High Obsidian UNC",
                R.drawable.nikecolor,
                listOf(R.drawable.nikecolor, R.drawable.nike, R.drawable.air1),
                listOf("36", "37", "38", "39","40","41","42","43","44")
            ),
            Product(
                "Converse",
                "115$",
                "Air Force 1 Shadow Beige Pale Ivory",
                R.drawable.converse,
                listOf(R.drawable.converse, R.drawable.air1, R.drawable.nikecolor),
                listOf("36", "37", "38", "39","40","41","42","43","44")
            )
        )

        val adapter = ImageAdapter(this, listProduct)
        binding.recyclerViewProducts.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewProducts.adapter = adapter
    }
}
