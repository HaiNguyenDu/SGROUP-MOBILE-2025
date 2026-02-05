package com.example.editphoto.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.graphics.withSave

class ImageSticker(private val bitmap: Bitmap): BaseSticker() {
    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(bitmap, matrix, paint)
    }

    override fun containsPoint(x: Float, y: Float): Boolean {
        val inverse = Matrix()
        matrix.invert(inverse)
        val pts = floatArrayOf(x, y)
        inverse.mapPoints(pts)
        return pts[0] in 0f..bitmap.width.toFloat() &&
                pts[1] in 0f..bitmap.height.toFloat()
    }
    override fun getLocalBounds(): RectF {
        return RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
    }
}