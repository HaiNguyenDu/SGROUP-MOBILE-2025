package com.example.sgroupmobile2025.ui

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.ui.RegisterActivity
import com.example.sgroupmobile2025.databinding.ActivityMainBinding
import com.example.sgroupmobile2025.databinding.LoginBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { LoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        initView()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val inset = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val layout = binding.edtUsername.layoutParams

            if (layout is ViewGroup.MarginLayoutParams) {
                layout.topMargin = inset.top
            }
            binding.edtUsername.layoutParams = layout


            WindowInsetsCompat.CONSUMED
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
                Toast.makeText(this@MainActivity, "Chúc mừng ní nghen :)))", Toast.LENGTH_SHORT).show()
            }
            register.setOnClickListener {
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}