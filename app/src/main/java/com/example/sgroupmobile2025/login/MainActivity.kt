package com.example.sgroupmobile2025.login

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.databinding.LoginBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { LoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        initView()
        initUi()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val inset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val layout = binding.edtUsername.layoutParams

            if (layout is ViewGroup.MarginLayoutParams) {
                layout.topMargin = inset.top
            }
            binding.edtUsername.layoutParams = layout
            WindowInsetsCompat.CONSUMED
        }

    }

    private fun initUi(){
        binding.apply {
//             Kiểm tra xem có phải máy ảo không
            val isEmulator = (android.os.Build.FINGERPRINT.startsWith("generic")
                    || android.os.Build.MODEL.contains("google_sdk")
                    || android.os.Build.MODEL.lowercase().contains("emulator")
                    || android.os.Build.MODEL.contains("Android SDK built for x86"))

            val imageRes = if (isEmulator) {
                R.drawable.user
            } else {
                R.drawable.av_main2
            }

            // Load ảnh bằng Glide
            Glide.with(this@MainActivity)
                .load(imageRes)
                .placeholder(R.drawable.user)
                .error(R.drawable.av_main2)
                .into(binding.imagemain)

//            //Đổi qua lại 2 ảnh với nhau nè ní
            var isFirst = true

            binding.imagemain.setOnClickListener {
                val newImage = if (isFirst) R.drawable.av_main2 else R.drawable.user
                Glide.with(this@MainActivity)
                    .load(newImage)
                    .into(binding.imagemain)

                isFirst = !isFirst
            }


        }
    }

    private fun initView() {
        binding.apply {
            signin.setOnClickListener {
                val username = edtUsername.text.toString().trim()
                val password = edtpassword.text.toString().trim()

                if (username.isEmpty()) {
                    edtUsername.error = "Nhập Username đi nào"
                    return@setOnClickListener
                }

                if (password.isEmpty()) {
                    edtpassword.error = "Nhập Password đi ní"
                    return@setOnClickListener
                }

                // Nếu pass login (giờ demo cho qua luôn)
                Toast.makeText(this@MainActivity, "Chúc mừng ní nghen :)))", Toast.LENGTH_SHORT).show()

                // 👉 Chuyển sang HomeActivity
                val intent = Intent(this@MainActivity, com.example.sgroupmobile2025.home.HomeActivity::class.java)
                startActivity(intent)
                finish() // để khi back không quay về login nữa
            }

            register.setOnClickListener {
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

}