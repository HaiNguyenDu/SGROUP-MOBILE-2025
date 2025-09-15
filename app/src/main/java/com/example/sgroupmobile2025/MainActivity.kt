package com.example.sgroupmobile2025

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.selects.SelectInstance

class MainActivity : AppCompatActivity(){
    //    private val binding by lazy { MainActivity }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState )
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)){v, insets ->
            val systemsBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemsBars.left, systemsBars.top, systemsBars.right, systemsBars.bottom)
            insets
        }
    }
}