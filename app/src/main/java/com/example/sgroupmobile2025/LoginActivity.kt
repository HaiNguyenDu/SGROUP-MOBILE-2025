package com.example.sgroupmobile2025

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sgroupmobile2025.databinding.ActivityLoginBinding
class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.cvSubmit.setOnClickListener {
            login()
        }

        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish() // finish login activity so user can't go back here
        }
    }

    private fun login() {
        val userName = "tphuong"
        val passWord = "123456"

        val inputUser = binding.etUsername.text.toString().trim()
        val inputPass = binding.etPw.text.toString().trim()

        if (inputUser == userName && inputPass == passWord) {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }
}
