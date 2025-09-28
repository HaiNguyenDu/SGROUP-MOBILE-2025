package com.example.sgroupmobile2025.detail

import ProductItem
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Typeface
import androidx.core.view.ViewCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater)}
    private var currentPrice = 0
    private var product: ProductItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val typeView = listOf(binding.tvEur, binding.tvUk, binding.tvUs)
        typeView.forEach { textView ->
            textView.setOnClickListener {
                setSingleSelection(typeView, textView)
            }
        }
        product = intent.getSerializableExtra("product") as? ProductItem
        //cach1: set type
//        binding.tvEur.setOnClickListener {
//            toggleTextStyle(binding.tvEur)
//        }
//        binding.tvUk.setOnClickListener {
//            toggleTextStyle(binding.tvUk)
//        }
//        binding.tvUs.setOnClickListener {
//            toggleTextStyle(binding.tvUs)
//        }
        val sizes = intent.getSerializableExtra("sizes") as? ArrayList<SizeItem> ?: arrayListOf(
            SizeItem("37"),
            SizeItem("38"),
            SizeItem("39"),
            SizeItem("40"),
            SizeItem("41"),
            SizeItem("42")
        )
        val sizeAdapter = SizeAdapter(sizes) { clickedSize ->
            sizes.forEach { it.isSelected = false }
            clickedSize.isSelected = true
            binding.productSize.adapter?.notifyDataSetChanged()
        }
        binding.productSize.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.productSize.adapter = sizeAdapter
        binding.vDetailBack.setOnClickListener {
            finish()
        }
        val product = intent.getSerializableExtra("product") as? ProductItem
        val position = intent.getIntExtra("position", -1)
        product?.let { productItem ->
            if (position != -1) productItem.isFavorite = (position % 2 != 0)
            binding.ivProduct.setImageResource(productItem.imageRes)
            binding.tvDetailBrand.text = productItem.brand
            binding.tvDetailDesBrand.text = productItem.description
            currentPrice = productItem.price.replace("$", "").toIntOrNull() ?: 0
            updatePrice()
            setHeartIcon(productItem.isFavorite)
            binding.ivHeart.setOnClickListener {
                productItem.isFavorite = !productItem.isFavorite
                setHeartIcon(productItem.isFavorite)
            }
        }

        binding.ivDetailPlus.setOnClickListener {
            currentPrice++
            updatePrice()
        }

        binding.ivDetailMinus.setOnClickListener {
            if (currentPrice > 1) {
                currentPrice--
                updatePrice()
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun updatePrice() {
        binding.tvDetailCost.text = "$$currentPrice"
    }
    //Cach1: set type
//    private fun toggleTextStyle(textView: TextView){
//        if(textView.typeface.isBold){
//            textView.setTypeface(null, Typeface.NORMAL)
//        } else {
//            textView.setTypeface(null, Typeface.BOLD)
//        }
//    }

    //Cach 2:
    private fun setSingleSelection(allViews: List<TextView>, selected: TextView){
        allViews.forEach {
            it.setTypeface(null, Typeface.NORMAL)
            it.setTextColor(getColor(R.color.black))
        }
        selected.setTypeface(null, Typeface.BOLD)
    }
    private fun setHeartIcon(isFavorite: Boolean){
        if(isFavorite){
            binding.ivHeart.setColorFilter(
                ContextCompat.getColor(this, R.color.red)
            )
        } else {
            binding.ivHeart.setColorFilter(
                ContextCompat.getColor(this, R.color.white)
            )
        }
    }
}