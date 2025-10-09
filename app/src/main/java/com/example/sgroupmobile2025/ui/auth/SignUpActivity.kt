package com.example.sgroupmobile2025.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sgroupmobile2025.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        // Back button
        binding.btnBack.setOnClickListener {
            finish()
        }
        // Submit button
        binding.cvSubmit.setOnClickListener {
            val noti = if (validateRegister()) "Sign up successful" else "Sign up failed"
            Toast.makeText(this, noti, Toast.LENGTH_SHORT).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val inset = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val layout = binding.btnBack.layoutParams
            if (layout is ViewGroup.MarginLayoutParams) {
                layout.topMargin = inset.top
            }
            binding.btnBack.layoutParams = layout
            WindowInsetsCompat.CONSUMED
        }
    }
    private fun validateRegister(): Boolean {
        val email = binding.etEmail.text.toString().trim()
        val pass = binding.etPw.text.toString().trim()
        val name = binding.etName.text.toString().trim()

        if (email.isEmpty() || pass.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Please fill all information", Toast.LENGTH_LONG).show()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Invalid email"
            return false
        }
        if (name.length < 2) {
            binding.etName.error = "Name must be at least 2 characters"
            return false
        }
        if (pass.length < 6) {
            binding.etPw.error = "Password must be at least 6 characters"
            return false
        }
        return true
    }
}