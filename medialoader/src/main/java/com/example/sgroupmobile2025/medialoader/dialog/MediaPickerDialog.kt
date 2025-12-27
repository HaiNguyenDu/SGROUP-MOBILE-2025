package com.sgroup.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.sgroupmobile2025.medialoader.databinding.DialogCustomBinding

class MediaPickerDialog(private val context: Context) {

    private lateinit var dialog: AlertDialog
    private lateinit var binding: DialogCustomBinding

    private var onCameraListener: (() -> Unit)? = null
    private var onGalleryListener: (() -> Unit)? = null
    private var onAudioListener: (() -> Unit)? = null
    private var onCancelListener: (() -> Unit)? = null

    fun showImagePickerDialog() {
        binding = DialogCustomBinding.inflate(LayoutInflater.from(context))

        binding.tvTitle.text = "Chọn Ảnh"
        binding.tvMessage.text = "Chọn nguồn ảnh:"
        binding.btnDialogConfirm.text = "Camera"
        binding.btnDialogCancel.text = "Gallery"

        dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        binding.btnDialogConfirm.setOnClickListener {
            onCameraListener?.invoke()
            dialog.dismiss()
        }

        binding.btnDialogCancel.setOnClickListener {
            onGalleryListener?.invoke()
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showAudioPickerDialog() {
        binding = DialogCustomBinding.inflate(LayoutInflater.from(context))

        binding.tvTitle.text = "Chọn Audio"
        binding.tvMessage.text = "Bạn muốn chọn audio từ đâu?"
        binding.btnDialogConfirm.text = "Ghi âm"
        binding.btnDialogCancel.text = "Chọn File"

        dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        binding.btnDialogConfirm.setOnClickListener {
            onAudioListener?.invoke()
            dialog.dismiss()
        }

        binding.btnDialogCancel.setOnClickListener {
            onCancelListener?.invoke()
            dialog.dismiss()
        }

        dialog.show()
    }

    fun setOnCameraListener(listener: () -> Unit) {
        onCameraListener = listener
    }

    fun setOnGalleryListener(listener: () -> Unit) {
        onGalleryListener = listener
    }

    fun setOnAudioListener(listener: () -> Unit) {
        onAudioListener = listener
    }

    fun setOnCancelListener(listener: () -> Unit) {
        onCancelListener = listener
    }

    fun dismiss() {
        if (::dialog.isInitialized) {
            dialog.dismiss()
        }
    }
}