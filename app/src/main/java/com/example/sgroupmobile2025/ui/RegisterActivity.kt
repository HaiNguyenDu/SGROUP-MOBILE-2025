package com.example.sgroupmobile2025.ui

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.databinding.ActivityMainBinding
import com.example.sgroupmobile2025.databinding.RegisterBinding
import kotlin.toString

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { RegisterBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        registerView()
        initUi()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root){v,insets ->
            val inset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val layout = binding.tvBack.layoutParams

            if(layout is ViewGroup.MarginLayoutParams){
                layout.topMargin = inset.top
            }
            binding.tvBack.layoutParams = layout


            WindowInsetsCompat.CONSUMED
        }


    }


    private fun initUi(){
        binding.apply {
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
            Glide.with(this@RegisterActivity)
                .load(imageRes)
                .placeholder(R.drawable.user)
                .error(R.drawable.av_main2)
                .into(binding.ivMain2)
        }
    }

    private fun registerView(){
        binding.apply {
            buttonContainer.setOnClickListener {
                val email = edtEmail.text.toString().trim()
                val yourname = edtYouName.text.toString().trim()
                val password = edtPassWord.text.toString().trim()

                if(email.isEmpty()){
                    edtEmail.error = "Thiếu email nè ní"
                    return@setOnClickListener
                }
                if(yourname.isEmpty()){
                    edtYouName.error = "Thiếu yourname nè ní"
                    return@setOnClickListener
                }
                if(password.isEmpty()){
                    edtPassWord.error = "Thiếu password nè ní"
                    return@setOnClickListener
                }
                Toast.makeText(this@RegisterActivity, "Chúc mừng ní nghen :)))", Toast.LENGTH_SHORT).show()

            }
            tvBack.setOnClickListener {
                finish()
            }
        }
    }
}

