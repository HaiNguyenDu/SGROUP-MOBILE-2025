package com.example.sgroupmobile2025

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sgroupmobile2025.databinding.ActivityProductBinding

class ProductActivity : AppCompatActivity() {
    private val binding by lazy { ActivityProductBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupRecyclerView()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val inset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val layout = binding.cvSearch.layoutParams
            if(layout is ViewGroup.MarginLayoutParams){
                layout.topMargin = inset.top
                layout.bottomMargin = inset.bottom
            }
            binding.cvSearch.layoutParams = layout
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupRecyclerView() {
        binding.rcv.layoutManager = GridLayoutManager(this, 2)
        binding.rcv.adapter = ProductAdapter(createProductList())
    }

    private fun createProductList(): List<DataProduct> {
        return listOf(
            DataProduct(R.drawable.product_1, "Converse", "Run Star Hike Three Color Unisex Hike Three Color Unisex", 85.5),
            DataProduct(R.drawable.product_2, "Nike", "Air Force 1 White", 120.0),
            DataProduct(R.drawable.product_3, "Adidas", "Ultraboost 22", 180.0),
            DataProduct(R.drawable.product_4, "Puma", "RS-X Toys", 95.0),
            DataProduct(R.drawable.product_1, "Vans", "Old Skool Black", 75.0),
            DataProduct(R.drawable.product_3, "Nike", "Air Force 1 Shadow Beige Pale Ivory", 115.0),
            DataProduct(R.drawable.product_2, "Nike", "Air Force 1 White", 120.0),
            DataProduct(R.drawable.product_3, "Adidas", "Ultraboost 22", 180.0),
            DataProduct(R.drawable.product_4, "Puma", "RS-X Toys", 95.0),
            DataProduct(R.drawable.product_1, "Vans", "Old Skool Black", 75.0),
            DataProduct(R.drawable.product_2, "Nike", "Air Force 1 White", 120.0),
            DataProduct(R.drawable.product_3, "Adidas", "Ultraboost 22", 180.0),
            DataProduct(R.drawable.product_4, "Puma", "RS-X Toys", 95.0),
            DataProduct(R.drawable.product_1, "Vans", "Old Skool Black", 75.0),

        )
    }
}