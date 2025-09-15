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

class MainActivity : AppCompatActivity() {
    //    private val binding by lazy { MainActivity }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemsBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemsBars.left, systemsBars.top, systemsBars.right, systemsBars.bottom)
            insets
        }
        // Nút Sign Up để chuyển sang Register
        val signUpButton = findViewById<Button>(R.id.button_sign_up)
        signUpButton.setOnClickListener {
            val intent = Intent(this, Register_Activity::class.java)
            startActivity(intent)
        }

        // Nút Sign In để hiện thông báo
        val signInButton = findViewById<Button>(R.id.button_sign_in)
        signInButton.setOnClickListener {
            Toast.makeText(this, "Sign In clicked!", Toast.LENGTH_SHORT).show()
        }
    }
}