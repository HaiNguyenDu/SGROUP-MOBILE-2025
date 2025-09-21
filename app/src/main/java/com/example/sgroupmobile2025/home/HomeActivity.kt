package com.example.sgroupmobile2025.home

import ImageAdapter
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
            Product(R.drawable.nike, "Nike", "Air Force 1 Jester XX Black Sonic Yellow...", "96$"),
            Product(R.drawable.air1, "Converse", "Run Star Hike Three Color Unisex", "85.5$"),
            Product(R.drawable.nikecolor, "Nike", "Air Jordan 1 Retro High Obsidian UNC", "196$"),
            Product(R.drawable.converse, "converse", "Air Force 1 Shadow Beige Pale Ivory", "115$"),
            Product(R.drawable.nike, "Nike", "Air Force 1 Jester XX Black Sonic Yellow...", "96$"),
            Product(R.drawable.air1, "Converse", "Run Star Hike Three Color Unisex", "85.5$"),
            Product(R.drawable.nikecolor, "Nike", "Air Jordan 1 Retro High Obsidian UNC", "196$"),
            Product(R.drawable.converse, "converse", "Air Force 1 Shadow Beige Pale Ivory", "115$")
        )

        val adapter = ImageAdapter(this, listProduct)
        binding.recyclerViewProducts.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewProducts.adapter = adapter
    }


}
