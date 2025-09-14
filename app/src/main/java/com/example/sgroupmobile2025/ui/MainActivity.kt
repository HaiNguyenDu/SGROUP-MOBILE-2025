package com.example.sgroupmobile2025.ui

import android.content.Intent
import android.os.Bundle
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

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login)

        // Tìm nút trong layout login.xml
        val edtUsername = findViewById<EditText>(R.id.edtUsername)
        val edtPassword = findViewById<EditText>(R.id.edtpassword)
        val btnSignIn = findViewById<Button>(R.id.signin)
        val btnRegister = findViewById<Button>(R.id.register)

        // Sự kiện bấm Sign In
        btnSignIn.setOnClickListener {
            val username = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (username.isEmpty()) {
                edtUsername.error = "Nhập Username đi nào"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                edtPassword.error = "Nhập Password đi ní"
                return@setOnClickListener
            }

            Toast.makeText(this, "Chúc mừng ní nghen :)))", Toast.LENGTH_SHORT).show()
        }


        // Sự kiện bấm Register
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

}