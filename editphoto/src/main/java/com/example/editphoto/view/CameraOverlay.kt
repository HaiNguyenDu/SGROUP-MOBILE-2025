package com.example.editphoto.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.example.editphoto.contants.RatioSize

class CameraOverlay(context: Context, atr: AttributeSet) : View(context, atr) {
    private var ratio = RatioSize.RATIO_4_3
    private var gridState = true
    private var currentOverlayHeight = 0f

    private val rectPaint = Paint().apply {
        color = Color.BLACK
        alpha = 150
        style = Paint.Style.FILL
    }

    private val whitePaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 4f
        alpha = 100
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        currentOverlayHeight = (w / ratio.ratio).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerY = height / 2f
        val topRectBottom = centerY - (currentOverlayHeight / 2f)
        val bottomRectTop = centerY + (currentOverlayHeight / 2f)

        canvas.drawRect(0f, 0f, width.toFloat(), topRectBottom, rectPaint)
        canvas.drawRect(0f, bottomRectTop, width.toFloat(), height.toFloat(), rectPaint)

        if (gridState) drawGrid(canvas, topRectBottom, bottomRectTop)
    }

    fun setGrid(state: Boolean) {
        gridState = state
        invalidate()
    }

    fun setSize(cameraSize: RatioSize) {
        val startHeight = currentOverlayHeight
        val endHeight = if (cameraSize == RatioSize.RATIO_16_9) {
            height.toFloat()
        } else {
            (width / cameraSize.ratio).toFloat()
        }

        ValueAnimator.ofFloat(startHeight, endHeight).apply {
            duration = 300
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                currentOverlayHeight = it.animatedValue as Float
                invalidate()
            }
            start()
        }
        this.ratio = cameraSize
    }

    private fun drawGrid(canvas: Canvas, top: Float, bottom: Float) {
        val viewWidth = width.toFloat()
        val viewHeight = bottom - top

        val colWidth = viewWidth / 3f
        val rowHeight = viewHeight / 3f
        Log.e("Sizeeeeee", "$colWidth $rowHeight $top $bottom")

        val lines = floatArrayOf(
            colWidth, top,
            colWidth, bottom,

            colWidth * 2, top,
            colWidth * 2, bottom,

            0f, top + rowHeight,
            viewWidth, top + rowHeight,

            0f, top + rowHeight * 2,
            viewWidth, top + rowHeight * 2
        )
        canvas.drawLines(lines, whitePaint)
    }
}