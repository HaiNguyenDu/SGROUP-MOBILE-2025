package com.example.sgroupmobile2025

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sgroupmobile2025.databinding.ActivityRegisterBinding
import kotlinx.coroutines.selects.SelectInstance

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.buttonBack.setOnClickListener {
            finish()
        }
        binding.apply {
            buttonBack.setOnClickListener {
                finish() //Huỷ màn hình hiện tại
            }
            buttonSignUpRegister.setOnClickListener {
                Toast.makeText(this@RegisterActivity, "Đăng ký nè!", Toast.LENGTH_LONG).show()
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val inset = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val layout = binding.buttonBack.layoutParams
            if (layout is ViewGroup.MarginLayoutParams) {
                layout.topMargin = inset.top
            }
            binding.buttonBack.layoutParams = layout

            WindowInsetsCompat.CONSUMED
        }
    }
}
