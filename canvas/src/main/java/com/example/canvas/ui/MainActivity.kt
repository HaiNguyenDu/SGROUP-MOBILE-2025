package com.example.canvas.ui

import android.app.Dialog
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.canvas.R
import com.example.canvas.adapter.ColorAdapter
import com.example.canvas.adapter.PenAdapter
import com.example.canvas.data.PenItem
import com.example.canvas.data.PenType
import com.example.canvas.databinding.ActivityMainBinding
import com.example.canvas.databinding.DialogColorPickerBinding
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val penItems = listOf(
        PenItem(PenType.PEN, R.drawable.pen),
        PenItem(PenType.PENCIL, R.drawable.pencil),
        PenItem(PenType.HIGHLIGHTER, R.drawable.highlighter),
        PenItem(PenType.ERASER, R.drawable.eraser)
    )
    private var currentSelected = 0
    private var adjustVisible = false
    private lateinit var adapter: PenAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUi()
        setupRecyclerView()
        setupActions()
    }

    private fun setupUi() {
        binding.drawView.setListener { redoSize, undoSize ->
            binding.btnUndo.apply {
                setImageResource(if (undoSize == 0) R.drawable.ic_undo else R.drawable.ic_undo_black)
                isClickable = undoSize != 0
                alpha = if (undoSize == 0) 0.5f else 1.0f
            }
            binding.btnRedo.apply {
                setImageResource(if (redoSize == 0) R.drawable.ic_redo else R.drawable.ic_redo_black)
                isClickable = redoSize != 0
                alpha = if (redoSize == 0) 0.5f else 1.0f
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = PenAdapter(penItems) { item, position ->
            if (currentSelected == position) {
                toggleAdjustPanel()
                return@PenAdapter
            }
            currentSelected = position
            adjustVisible = false
            binding.adjustPanel.visibility = View.GONE
            binding.drawView.changePaintTool(item.type)
        }

        binding.rvTools.apply {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
        }
    }

    private fun setupActions() {
        binding.btnColor1.setOnClickListener { showColorPicker(false) }
        binding.btnColor2.setOnClickListener { showColorPicker(true) }
        binding.btnUndo.setOnClickListener { binding.drawView.undo() }
        binding.btnRedo.setOnClickListener { binding.drawView.redo() }
        binding.btnAdd.setOnClickListener { pickImageLauncher.launch("image/*") }
        binding.btnSave.setOnClickListener { saveBitmapToGallery(binding.drawView.getBitmapFromView()) }
        binding.btnMore.setOnClickListener { showAdjustImagePanel(true) }

        binding.btnCancelAdjust.setOnClickListener {
            binding.drawView.changeAlphaView(255)
            binding.drawView.changeBrightness(0)
            binding.seekBrightness.progress = 255
            binding.seekImageAlpha.progress = 255
            showAdjustImagePanel(false)
        }

        binding.btnConfirmAdjust.setOnClickListener { showAdjustImagePanel(false) }

        binding.seekStroke.progress = 10
        binding.seekStroke.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, p: Int, f: Boolean) {
                binding.drawView.changeStroke(p.toFloat())
                binding.txtStrokeValue.text = p.toString()
            }
            override fun onStartTrackingTouch(s: SeekBar?) {}
            override fun onStopTrackingTouch(s: SeekBar?) {}
        })

        binding.seekAlpha.progress = 255
        binding.seekAlpha.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, p: Int, f: Boolean) {
                binding.drawView.changeAlpha(p)
                val percent = (p * 100) / 255
                binding.txtAlphaValue.text = "$percent%"
            }
            override fun onStartTrackingTouch(s: SeekBar?) {}
            override fun onStopTrackingTouch(s: SeekBar?) {}
        })

        binding.seekImageAlpha.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, p: Int, f: Boolean) {
                binding.drawView.changeAlphaView(p)
            }
            override fun onStartTrackingTouch(s: SeekBar?) {}
            override fun onStopTrackingTouch(s: SeekBar?) {}
        })

        binding.seekBrightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, p: Int, f: Boolean) {
                val brightness = p - 255 // range -255 to 255
                binding.drawView.changeBrightness(brightness)
            }
            override fun onStartTrackingTouch(s: SeekBar?) {}
            override fun onStopTrackingTouch(s: SeekBar?) {}
        })
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { binding.drawView.setImageBitmap(uriToBitmap(it)) }
    }

    private fun uriToBitmap(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.isMutableRequired = true
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            }
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }
    }

    private fun saveBitmapToGallery(bitmap: Bitmap) {
        val filename = "IMG_${System.currentTimeMillis()}.png"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        val uri = contentResolver.insert(EXTERNAL_CONTENT_URI, values) ?: return
        contentResolver.openOutputStream(uri)?.use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        values.clear()
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        contentResolver.update(uri, values, null, null)
        showSavedAnimation()
    }

    private fun showSavedAnimation() {
        binding.btnSave.apply {
            isClickable = false
            setImageResource(R.drawable.ic_check)
            startAnimation(AnimationUtils.loadAnimation(this@MainActivity, R.anim.anim_save_success))
            postDelayed({
                setImageResource(R.drawable.ic_download)
                isClickable = true
            }, 1600)
        }
    }

    private fun toggleAdjustPanel() {
        adjustVisible = !adjustVisible
        binding.adjustPanel.visibility = if (adjustVisible) View.VISIBLE else View.GONE
    }

    private fun showColorPicker(isBackgroundColor: Boolean) {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val dialogBinding = DialogColorPickerBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        val colorList = listOf(
            0xFFFFFFFF.toInt(), 0xFFE5E5E5.toInt(), 0xFFD1D1D1.toInt(), 0xFFBDBDBD.toInt(), 0xFFA9A9A9.toInt(), 0xFF8E8E93.toInt(),
            0xFF757575.toInt(), 0xFF616161.toInt(), 0xFF424242.toInt(), 0xFF212121.toInt(), 0xFF121212.toInt(), 0xFF000000.toInt(),
            0xFF001A33.toInt(), 0xFF003366.toInt(), 0xFF000080.toInt(), 0xFF330066.toInt(), 0xFF660066.toInt(), 0xFF990033.toInt(),
            0xFF990000.toInt(), 0xFF663300.toInt(), 0xFF333300.toInt(), 0xFF003300.toInt(), 0xFF003333.toInt(), 0xFF001F1F.toInt(),
            0xFF004080.toInt(), 0xFF0059B3.toInt(), 0xFF0000FF.toInt(), 0xFF6600CC.toInt(), 0xFFCC00CC.toInt(), 0xFFFF0066.toInt(),
            0xFFFF0000.toInt(), 0xFFFF8000.toInt(), 0xFFFFFF00.toInt(), 0xFF00FF00.toInt(), 0xFF00FFFF.toInt(), 0xFF008080.toInt(),
            0xFF007AFF.toInt(), 0xFF5856D6.toInt(), 0xFFAF52DE.toInt(), 0xFFFF2D55.toInt(), 0xFFFF3B30.toInt(), 0xFFFF9500.toInt(),
            0xFFFFCC00.toInt(), 0xFF34C759.toInt(), 0xFF5AC8FA.toInt(), 0xFF00C7BE.toInt(), 0xFF30B0C7.toInt(), 0xFF32ADE6.toInt(),
            0xFFCCE5FF.toInt(), 0xFFE5CCFF.toInt(), 0xFFFFCCFF.toInt(), 0xFFFFCCE5.toInt(), 0xFFFFCCCC.toInt(), 0xFFFFE5CC.toInt(),
            0xFFFFFFCC.toInt(), 0xFFCCFFCC.toInt(), 0xFFCCFFFF.toInt(), 0xFFB2EBF2.toInt(), 0xFFE0F7FA.toInt(), 0xFFF1F8E9.toInt()
        )

        var currentColor = if (isBackgroundColor) 0xFFFFFFFF.toInt() else 0xFF000000.toInt()

        dialogBinding.viewColorPreview.setBackgroundColor(currentColor)
        dialogBinding.txtColorHex.text = String.format("#%06X", (0xFFFFFF and currentColor))

        dialogBinding.rvColorGrid.apply {
            layoutManager = androidx.recyclerview.widget.GridLayoutManager(this@MainActivity, 10)
            adapter = ColorAdapter(colorList) { selectedColor ->
                currentColor = selectedColor
                dialogBinding.viewColorPreview.setBackgroundColor(selectedColor)
                dialogBinding.txtColorHex.text = String.format("#%06X", (0xFFFFFF and selectedColor))
            }
        }
        dialogBinding.btnSelectColor.setOnClickListener {
            if (isBackgroundColor) binding.drawView.changeBackgroundColor(currentColor)
            else binding.drawView.changeColor(currentColor)
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.show()
    }

    private fun showAdjustImagePanel(show: Boolean) {
        binding.toolPanel.visibility = if (show) View.GONE else View.VISIBLE
        binding.adjustImagePanel.visibility = if (show) View.VISIBLE else View.GONE
        if (show) binding.adjustPanel.visibility = View.GONE
    }
}