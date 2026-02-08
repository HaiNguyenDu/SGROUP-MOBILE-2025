package com.example.editphoto.ui

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.example.editphoto.contants.Contants.REQUIRED_PERMISSIONS
import com.example.editphoto.contants.RatioSize
import com.example.editphoto.databinding.ActivityCameraBinding
import java.text.SimpleDateFormat
import java.util.*
import com.example.editphoto.R


class CameraActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }
    private var imageCapture: ImageCapture? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var isGridOn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupClickListeners()
        if (allPermissionsGranted()) startCamera() else requestPermissions()
    }

    private fun setupClickListeners() {
        binding.btnClose.setOnClickListener { finish() }

        binding.btnSwap.setOnClickListener {
            lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK)
                CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK
            startCamera()
        }

        binding.btnGrid.setOnClickListener {
            isGridOn = !isGridOn
            binding.cameraOverlay.setGrid(isGridOn)
            if(isGridOn)
                binding.tvGrid.setImageResource(R.drawable.ic_grid_on)
            else binding.tvGrid.setImageResource(R.drawable.ic_grid_off)
        }

        binding.btnSize.setOnClickListener {
            binding.layoutSize.visibility = if (binding.layoutSize.isGone)
                View.VISIBLE else View.GONE
        }

        binding.option11.setOnClickListener { updateRatio(RatioSize.RATIO_1_1, "1:1") }
        binding.option43.setOnClickListener { updateRatio(RatioSize.RATIO_4_3, "4:3") }
        binding.option169.setOnClickListener { updateRatio(RatioSize.RATIO_16_9, "16:9") }

        binding.btnCamera.setOnClickListener { takePhoto() }
    }

    private fun updateRatio(ratio: RatioSize, label: String) {
        binding.textCurrentSize.text = label
        binding.cameraOverlay.setSize(ratio)
        binding.layoutSize.visibility = View.GONE
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Toast.makeText(this, "Lỗi khởi tạo camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun openMainActivity(uri: android.net.Uri) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("IMAGE_URI", uri.toString())
        }
        startActivity(intent)
        finish()
    }
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/EditPhoto")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).build()

        binding.root.foreground = ColorDrawable(Color.WHITE)
        binding.root.postDelayed({ binding.root.foreground = null }, 100)

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: return

                    openMainActivity(savedUri)
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(baseContext, "Lỗi: ${exc.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions.all { it.value }) startCamera() else finish()
    }
}