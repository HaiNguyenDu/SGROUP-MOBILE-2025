package com.example.editphoto.ui

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.graphics.ImageDecoder
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.editphoto.R
import com.example.editphoto.databinding.ActivityMainBinding
import com.example.editphoto.model.ImageSticker
import com.example.editphoto.model.TextSticker

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val stickerList by lazy {
        listOf(
            "sticker1.png","sticker2.png","sticker3.png"
        ).mapNotNull { loadBitmapFromAssets(this, it) }
    }

    private val fontList = listOf(
        "fonts/VHAVANB.TTF",
        "fonts/VHCENTN.TTF",
        "fonts/VHKOALA.TTF"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupBackgroundImage()
        setupEvents()
    }

    private fun setupBackgroundImage() {

        intent.getStringExtra("IMAGE_URI")?.let {

            val bitmap = loadBitmapFromUri(it.toUri())
            binding.editorView.setBackgroundBitmap(bitmap)
        }
    }

    private fun setupEvents() {

        binding.btnBack.setOnClickListener { finish() }

        binding.btnSave.setOnClickListener {
            saveBitmapToGallery(binding.editorView.exportBitmap())
        }

        binding.btnSticker.setOnClickListener {
            showStickerSelection()
        }

        binding.btnText.setOnClickListener {
            showFontSelection()
        }
    }

    private fun showFontSelection() {

        binding.selectionPanel.visibility = View.VISIBLE
        binding.selectionContainer.removeAllViews()

        for (fontPath in fontList) {

            val tv = TextView(this)

            tv.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                dp(70)
            ).apply { marginEnd = dp(20) }

            tv.text = "Aa"
            tv.textSize = 24f
            tv.gravity = Gravity.CENTER
            tv.setTextColor(Color.WHITE)

            val tf = Typeface.createFromAsset(assets, fontPath)
            tv.typeface = tf

            tv.setOnClickListener {

                showTextInputDialog(tf)
            }

            binding.selectionContainer.addView(tv)
        }
    }

    private fun showTextInputDialog(selectedFont: Typeface) {

        val view = layoutInflater.inflate(R.layout.dialog_text_input, null)

        val edt = view.findViewById<EditText>(R.id.edtText)
        val btnOk = view.findViewById<TextView>(R.id.btnOk)
        val btnCancel = view.findViewById<TextView>(R.id.btnCancel)

        edt.typeface = selectedFont

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()

        dialog.show()

        btnOk.setOnClickListener {

            val text = edt.text.toString()

            if (text.isNotEmpty()) {

                val sticker = TextSticker(text)
                sticker.setTypeface(selectedFont)

                binding.editorView.addSticker(sticker)
            }

            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        edt.requestFocus()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT)
    }


    private fun showStickerSelection() {

        binding.selectionPanel.visibility = View.VISIBLE
        binding.selectionContainer.removeAllViews()

        for (bitmap in stickerList) {

            val imageView = ImageView(this)

            imageView.layoutParams =
                LinearLayout.LayoutParams(dp(80), dp(80)).apply {
                    marginEnd = dp(12)
                }

            imageView.setImageBitmap(bitmap)

            imageView.setOnClickListener {

                binding.editorView.addSticker(ImageSticker(bitmap))
                binding.selectionPanel.visibility = View.GONE
            }

            binding.selectionContainer.addView(imageView)
        }
    }


    private fun loadBitmapFromAssets(context: Context, fileName: String): Bitmap? {

        return try {
            val inputStream = context.assets.open(fileName)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }

    private fun loadBitmapFromUri(uri: Uri): Bitmap {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            val source = ImageDecoder.createSource(contentResolver, uri)

            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }
    }

    private fun saveBitmapToGallery(bitmap: Bitmap) {

        val filename = "IMG_${System.currentTimeMillis()}.png"

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        }

        val uri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        ) ?: return

        contentResolver.openOutputStream(uri)?.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }

    private fun dp(value: Int) =
        (value * resources.displayMetrics.density).toInt()
}