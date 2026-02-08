package com.example.editphoto.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.editphoto.contants.Contants.REQUIRED_PERMISSIONS
import com.example.editphoto.databinding.ActivityStartBinding
import java.io.File

class StartActivity : AppCompatActivity() {

    private val binding by lazy { ActivityStartBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpAction()
    }

    private fun setUpAction() {

        binding.btnOpenGallery.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnOpenCamera.setOnClickListener {
            openCameraActivity()
        }
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

            uri?.let {
                openMainActivity(it)
            }
        }
    private fun openMainActivity(uri: Uri) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("IMAGE_URI", uri.toString())
        startActivity(intent)
    }
    private fun openCameraActivity() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }
}