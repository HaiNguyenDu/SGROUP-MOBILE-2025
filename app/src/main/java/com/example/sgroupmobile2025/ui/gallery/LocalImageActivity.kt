package com.example.sgroupmobile2025.ui.gallery

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sgroupmobile2025.common.constants.constants.CAMERA_REQUEST_CODE
import com.example.sgroupmobile2025.common.constants.constants.GALLERY_REQUEST_CODE
import com.example.sgroupmobile2025.data.local.LocalData
import com.example.sgroupmobile2025.databinding.ActivityLocalImageBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.sgroup.dialog.MediaPickerDialog


class LocalImageActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLocalImageBinding.inflate(layoutInflater) }
    private lateinit var mediaDialog: MediaPickerDialog
    private val imagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        when {
            granted -> {
                binding.cardPermission.visibility = View.GONE
                showImagePickerDialog()
            }
            shouldShowRequestPermissionRationale(imagePermission) -> showRationale()
            else -> showDialogGoToSetting()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mediaDialog = MediaPickerDialog(this)

        val hasPermission = checkSelfPermission(imagePermission) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            showImagePickerDialog()
            binding.cardPermission.visibility = View.GONE
        } else {
            permissionLauncher.launch(imagePermission)
        }

        binding.btnRequestPermission.setOnClickListener {
            permissionLauncher.launch(imagePermission)
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun loadImages() {
        lifecycleScope.launch {
            try {
                binding.progressBar.visibility = View.VISIBLE
                binding.tvNoImage.visibility = View.GONE
                binding.rvImages.visibility = View.GONE

                // Giả lập thời gian tải ảnh
                delay(1500)

                val localImage = LocalData(this@LocalImageActivity)
                val images = localImage.getImages()

                if (images.isEmpty()) {
                    binding.tvNoImage.visibility = View.VISIBLE
                } else {
                    val adapter = LocalImageAdapter(images)
                    binding.rvImages.layoutManager =
                        GridLayoutManager(this@LocalImageActivity, 4)
                    binding.rvImages.adapter = adapter
                    binding.rvImages.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("LoadImageError", e.message ?: "Unknown error")
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
    private fun showImagePickerDialog() {
        mediaDialog.apply {
            setOnCameraListener {
                openCamera()
            }

            setOnGalleryListener {
                loadImages()
            }

            showImagePickerDialog()
        }
    }
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }


    private fun showRationale() {
        AlertDialog.Builder(this)
            .setTitle("Cần quyền truy cập ảnh")
            .setMessage("Ứng dụng cần quyền này để hiển thị hình ảnh từ thư viện.")
            .setPositiveButton("Đồng ý") { dialog, _ ->
                permissionLauncher.launch(imagePermission)
                dialog.dismiss()
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDialogGoToSetting() {
        AlertDialog.Builder(this)
            .setTitle("Quyền bị từ chối vĩnh viễn")
            .setMessage("Ứng dụng cần quyền truy cập ảnh. Vui lòng bật quyền trong Cài đặt.")
            .setPositiveButton("Đi tới Cài đặt") { dialog, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
