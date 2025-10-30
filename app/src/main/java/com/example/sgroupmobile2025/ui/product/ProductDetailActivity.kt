package com.example.sgroupmobile2025.ui.product

import android.R
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.common.constants.IntentKeys
import com.example.sgroupmobile2025.data.model.DataProduct
import com.example.sgroupmobile2025.data.repository.ProductRepository
import com.example.sgroupmobile2025.databinding.ActivityProductDetailBinding
import com.example.sgroupmobile2025.ui.product.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityProductDetailBinding.inflate(layoutInflater) }
    private val listSize = listOf<Int>(37, 38, 39, 40, 41, 42, 43, 44, 45)
    private var product: DataProduct? = null
    private lateinit var productViewModel: ProductViewModel
    private var isClicked = false
    private val adapter = SizeAdapter(listSize)
    private val listProduct = ProductRepository.products
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        initUI()
        handleViewCompat()
        setUpView()
        setOnCLick()
        handleObserver()
    }
    fun initUI(){
        val index = intent.getIntExtra(IntentKeys.PRODUCT_POSITION, -1)
        if(index < listProduct.size -1 && index >= 0) {
            product = listProduct[index]
            if(index % 2 == 1) changeColorIcon()
        }
        productViewModel = ProductViewModel(application)
        productViewModel.setPrice(product!!.getPrice())
    }
    fun handleViewCompat(){
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            binding.layoutMain.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun setOnCLick(){
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.icHeart.setOnClickListener {
            changeColorIcon()
        }
        binding.ivIcAdd.setOnClickListener {
            productViewModel.increasePrice()
        }

        binding.ivIcMinus.setOnClickListener {
            productViewModel.decreasePrice()
        }
    }

    fun handleObserver(){
        lifecycleScope.launch {
            productViewModel.price.collect { price ->
                binding.tvProductPrice.text = price.toString()
            }
        }
    }
    fun changeColorIcon(){
        if (isClicked) {
            binding.icHeart.setColorFilter(ContextCompat.getColor(this, R.color.white))
        } else {
            binding.icHeart.setColorFilter(ContextCompat.getColor(this, R.color.holo_red_light))
        }
        isClicked = !isClicked
    }
    fun setUpView(){
        if(product != null){
            Glide.with(this)
                .load(product!!.getImgSrc())
                .into(binding.ivProduct)
            binding.tvProductName.text = product!!.getName()
            binding.tvProductPrice.text = "$${product!!.getPrice()}"
            binding.tvProductDes.text = product!!.getDescription()
            binding.tvProductDetailDes.text = product!!.getDetailDes()
        }
        binding.rcvSize.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rcvSize.adapter = adapter
    }
}