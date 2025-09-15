package com.example.sgroupmobile2025

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.selects.SelectInstance
import android.widget.Button
import android.content.Intent
import android.widget.Toast
import com.example.sgroupmobile2025.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemsBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            insets
        }

        //Cach 1:
//        // Nút Sign Up để chuyển sang Register
//
//        binding.buttonSignUp.setOnClickListener {
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
//        }
//
//        // Nút Sign In để hiện thông báo
//        binding.buttonSignIn.setOnClickListener {
//            Toast.makeText(this, "Đăng nhập đi!", Toast.LENGTH_SHORT).show()
//        }

        //Cach 2:
        binding.apply {
            buttonSignUp.setOnClickListener {
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            // Nút Sign In để hiện thông báo
            buttonSignIn.setOnClickListener {
                Toast.makeText(this@MainActivity, "Đăng nhập đi!", Toast.LENGTH_LONG).show()
            }
        }
    }
}