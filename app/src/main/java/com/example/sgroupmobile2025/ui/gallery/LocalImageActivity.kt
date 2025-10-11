package com.example.sgroupmobile2025.ui.gallery

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.data.local.LocalData
import com.example.sgroupmobile2025.databinding.ActivityLocalImageBinding
import kotlinx.coroutines.launch

class LocalImageActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLocalImageBinding.inflate(layoutInflater) }
    val IMAGE_PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        granted ->
        when{
            granted ->{
                binding.cardPermission.visibility = View.GONE
                loadImages()
            }
            shouldShowRequestPermissionRationale(IMAGE_PERMISSION) -> showRationale()
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
        val hasPermission = checkSelfPermission(IMAGE_PERMISSION) == android.content.pm.PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            loadImages()
            binding.cardPermission.visibility = View.GONE
        } else {
            binding.cardPermission.visibility = View.VISIBLE

        }
        binding.btnRequestPermission.setOnClickListener {
            permissionLauncher.launch(IMAGE_PERMISSION)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }

    }
    fun loadImages(){
        lifecycleScope.launch {
            val localImage = LocalData(this@LocalImageActivity)
            val images = localImage.getImages()
            val adapter = LocalImageAdapter(images)
            Log.e("EEEE", images.toString())
            binding.rvImages.layoutManager = GridLayoutManager(this@LocalImageActivity, 4)
            binding.rvImages.adapter = adapter
        }
    }
    private fun showRationale() {
        AlertDialog.Builder(this)
            .setTitle("Cần quyền truy cập ảnh")
            .setMessage("App cần quyền này để hiển thị hình ảnh từ thư viện.")
            .setPositiveButton("Đồng ý") { dialog, _ ->
                // Yêu cầu quyền lại
                permissionLauncher.launch(IMAGE_PERMISSION)
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
            .setMessage("App cần quyền truy cập ảnh. Vui lòng bật quyền trong Cài đặt.")
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