package com.example.editphoto.model

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF

abstract class BaseSticker {
    val matrix = Matrix()
    var isSelected = false
    abstract fun draw(canvas: Canvas, paint: Paint)
    abstract fun containsPoint(x: Float, y: Float): Boolean

    fun getMappedPoints(): FloatArray {
        val bounds = getLocalBounds()
        val pts = floatArrayOf(
            bounds.left, bounds.top,      // 0, 1: Top-Left
            bounds.right, bounds.top,     // 2, 3: Top-Right
            bounds.right, bounds.bottom,  // 4, 5: Bottom-Right
            bounds.left, bounds.bottom    // 6, 7: Bottom-Left
        )
        matrix.mapPoints(pts)
        return pts
    }

    abstract fun getLocalBounds(): RectF

    fun getBounds(): RectF {
        val rect = getLocalBounds()
        matrix.mapRect(rect)
        return rect
    }
}