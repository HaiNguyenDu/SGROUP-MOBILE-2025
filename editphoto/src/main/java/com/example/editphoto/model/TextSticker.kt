package com.example.editphoto.model

import android.R.attr.bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.core.graphics.withSave
import androidx.core.graphics.withMatrix

class TextSticker(
    var text: String,
) : BaseSticker() {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 80f
        typeface = Typeface.DEFAULT_BOLD
    }

    private val textBounds = RectF()

    private fun updateBounds() {

        val width = textPaint.measureText(text)

        val fontMetrics = textPaint.fontMetrics
        val height = fontMetrics.bottom - fontMetrics.top

        textBounds.set(
            0f,
            0f,
            width,
            height
        )
    }

    override fun draw(canvas: Canvas, paint: Paint) {

        updateBounds()
        canvas.withMatrix(matrix) {
            drawText(text, 0f, textBounds.height(), textPaint)
        }
    }
    fun setTypeface(tf: Typeface) {
        textPaint.typeface = tf
        updateBounds()
    }

    override fun containsPoint(x: Float, y: Float): Boolean {

        val inverse = Matrix()
        matrix.invert(inverse)

        val pts = floatArrayOf(x, y)
        inverse.mapPoints(pts)

        return pts[0] in 0f..textBounds.width() &&
                pts[1] in 0f..textBounds.height()
    }

    override fun getLocalBounds(): RectF {
        updateBounds()
        return RectF(textBounds)
    }
}