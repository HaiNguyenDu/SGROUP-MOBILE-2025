package com.example.sgroupmobile2025.ui.product

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.common.constants.IntentKeys
import com.example.sgroupmobile2025.data.model.DataProduct
import com.example.sgroupmobile2025.data.repository.ProductRepository
import com.example.sgroupmobile2025.databinding.ActivityProductBinding
import com.example.sgroupmobile2025.ui.chat.ChatActivity
import com.example.sgroupmobile2025.ui.contacts.ContactsActivity
import com.example.sgroupmobile2025.ui.gallery.LocalImageActivity
import com.example.sgroupmobile2025.ui.map.MapActivity
import com.example.sgroupmobile2025.ui.music.MusicPlayerActivity
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

class ProductShowActivity : AppCompatActivity() {
    private val binding by lazy { ActivityProductBinding.inflate(layoutInflater) }
    private lateinit var productList: List<DataProduct>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MapLibre.getInstance(this, null, WellKnownTileServer.MapLibre)
        setContentView(binding.root)
        productList = createDataProducts()
        handleViewCompat()
        setupRecyclerView()
        setOnClick()
    }
    fun handleViewCompat(){
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val inset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val layout = binding.cvSearch.layoutParams
            if (layout is ViewGroup.MarginLayoutParams) {
                layout.topMargin = inset.top
                layout.bottomMargin = inset.bottom
            }
            binding.cvSearch.layoutParams = layout
            WindowInsetsCompat.CONSUMED
        }
    }
    fun setOnClick(){
        binding.icNav.setOnClickListener {
            val intentImage = Intent(this, LocalImageActivity::class.java)
            startActivity(intentImage)
        }
        binding.icContacts.setOnClickListener {
            val intentContacts = Intent(this, ContactsActivity::class.java)
            startActivity(intentContacts)
        }
        binding.icMap.setOnClickListener {
            val intentMap = Intent(this, MapActivity::class.java)
            startActivity(intentMap)
        }
        binding.icChat.setOnClickListener {
            val intentChat = Intent(this, ChatActivity::class.java)
            startActivity(intentChat)
        }
        binding.btnMusicPlayer.setOnClickListener {
            val intentMusic = Intent(this, MusicPlayerActivity::class.java)
            startActivity(intentMusic)
        }
    }

    private fun setupRecyclerView() {
        binding.rcv.layoutManager = GridLayoutManager(this, 2)
        binding.rcv.adapter = ProductAdapter(productList){position ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra(IntentKeys.PRODUCT_POSITION, position)
            startActivity(intent)
        }
    }

    private fun createDataProducts(): List<DataProduct> {
        ProductRepository.addProduct(
            DataProduct(
                R.drawable.shoe_1,
                "Converse",
                "Run Star Hike Three Color Unisex Hike Three Color Unisex",
                85.5,
                "Giày Converse Run Star Hike với thiết kế Three Color, phong cách unisex, mang lại sự trẻ trung và năng động."
            )
        )
        ProductRepository.addProduct(
            DataProduct(
                R.drawable.shoe_4,
                "Nike",
                "Air Force 1 White",
                120.0,
                "Nike Air Force 1 White – biểu tượng giày sneaker kinh điển với tone màu trắng tinh tế, dễ phối đồ."
            )
        )
        ProductRepository.addProduct(
            DataProduct(
                R.drawable.shoe_3,
                "Adidas",
                "Ultraboost 22",
                180.0,
                "Adidas Ultraboost 22 mang lại sự êm ái tuyệt vời với công nghệ đế Boost, hỗ trợ vận động và thời trang."
            )
        )
        ProductRepository.addProduct(
            DataProduct(
                R.drawable.shoe_4,
                "Puma",
                "RS-X Toys",
                95.0,
                "Giày Puma RS-X Toys lấy cảm hứng từ phong cách retro, đế chunky hiện đại, phù hợp giới trẻ cá tính."
            )
        )
        ProductRepository.addProduct(
            DataProduct(
                R.drawable.shoe_1,
                "Vans",
                "Old Skool Black",
                75.0,
                "Vans Old Skool Black – đôi giày skate huyền thoại với đường viền trắng đặc trưng, bền bỉ và cá tính."
            )
        )
        ProductRepository.addProduct(
            DataProduct(
                R.drawable.shoe_3,
                "Nike",
                "Air Force 1 Shadow Beige Pale Ivory",
                115.0,
                "Nike Air Force 1 Shadow Beige Pale Ivory với thiết kế layer độc đáo, màu sắc tinh tế, phù hợp phong cách trẻ trung."
            )
        )
        ProductRepository.addProduct(
            DataProduct(
                R.drawable.shoe_1,
                "Nike",
                "Air Force 1 White",
                120.0,
                "Một phiên bản khác của Nike Air Force 1 White – giày sneaker đơn giản nhưng đầy cá tính, dễ kết hợp nhiều outfit."
            )
        )
        ProductRepository.addProduct(
            DataProduct(
                R.drawable.shoe_3,
                "Adidas",
                "Ultraboost 22",
                180.0,
                "Ultraboost 22 với thiết kế tối ưu cho người chạy bộ và di chuyển nhiều, mang lại sự thoải mái suốt ngày dài."
            )
        )
        ProductRepository.addProduct(
            DataProduct(
                R.drawable.shoe_4,
                "Puma",
                "RS-X Toys",
                95.0,
                "Puma RS-X Toys màu sắc trẻ trung, thiết kế chunky hiện đại, phù hợp streetwear."
            )
        )
        ProductRepository.addProduct(
            DataProduct(
                R.drawable.shoe_1,
                "Vans",
                "Old Skool Black",
                75.0,
                "Phiên bản Vans Old Skool Black – đơn giản, cá tính và dễ phối đồ."
            )
        )
        return ProductRepository.products
    }
}