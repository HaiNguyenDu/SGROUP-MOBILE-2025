package com.example.sgroupmobile2025.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.databinding.ActivityMainBinding

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register) // file register.xml

        val edtEmail = findViewById<EditText>(R.id.edt_email)
        val edtYourName = findViewById<EditText>(R.id.edt_you_name)
        val edtPassWord = findViewById<EditText>(R.id.edt_pass_word)
        val btnSignUp = findViewById<Button>(R.id.mtr_signup)
        val tvback = findViewById<TextView>(R.id.tv_back)
        btnSignUp.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val yourname = edtYourName.text.toString().trim()
            val password = edtPassWord.text.toString().trim()

            if(email.isEmpty()){
                edtEmail.error = "Thiếu email nè ní"
                return@setOnClickListener
            }
            if(yourname.isEmpty()){
                edtYourName.error = "Thiếu yourname nè ní"
                return@setOnClickListener
            }
            if(password.isEmpty()){
                edtPassWord.error = "Thiếu password nè ní"
                return@setOnClickListener
            }
            Toast.makeText(this, "Chúc mừng ní nghen :))", Toast.LENGTH_SHORT).show()
        }

        tvback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}