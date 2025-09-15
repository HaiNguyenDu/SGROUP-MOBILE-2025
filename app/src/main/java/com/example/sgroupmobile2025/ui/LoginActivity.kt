package com.example.sgroupmobile2025.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

    }
}