package com.example.editphoto.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.editphoto.contants.Contants.DRAG
import com.example.editphoto.contants.Contants.NONE
import com.example.editphoto.contants.Contants.RESIZE_ROTATE
import com.example.editphoto.model.BaseSticker
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.min

class EditorView(context: Context, atr: AttributeSet) : View(context, atr) {
    private val stickers = mutableListOf<BaseSticker>()
    private var selectedSticker: BaseSticker? = null

    private val imageRect = RectF()
    private var backgroundImage: Bitmap? = null
    private var scaledBackground: Bitmap? = null

    private var lastX = 0f
    private var lastY = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    private val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
        color = Color.WHITE
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
    }

    private var mode = NONE

    fun addSticker(sticker: BaseSticker) {
        // Đặt sticker vào trung tâm view
        val bounds = sticker.getLocalBounds()
        val startX = (width - bounds.width()) / 2f
        val startY = (height - bounds.height()) / 2f

        sticker.matrix.postTranslate(startX, startY)

        stickers.add(sticker)
        stickers.forEach { it.isSelected = false }
        sticker.isSelected = true
        selectedSticker = sticker
        invalidate()
    }

    fun exportBitmap(): Bitmap {
        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)

        canvas.clipRect(imageRect)
        draw(canvas)
        return resultBitmap
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.clipRect(imageRect)

        scaledBackground?.let {
            canvas.drawBitmap(it, imageRect.left, imageRect.top, paint)
        }

        for (sticker in stickers) {
            sticker.draw(canvas, paint)

            if (sticker.isSelected) {
                val pts = sticker.getMappedPoints()
                canvas.drawLine(pts[0], pts[1], pts[2], pts[3], borderPaint)
                canvas.drawLine(pts[2], pts[3], pts[4], pts[5], borderPaint)
                canvas.drawLine(pts[4], pts[5], pts[6], pts[7], borderPaint)
                canvas.drawLine(pts[6], pts[7], pts[0], pts[1], borderPaint)

                val oldEffect = borderPaint.pathEffect
                borderPaint.pathEffect = null
                canvas.drawCircle(pts[0], pts[1], 30f, borderPaint)
                canvas.drawCircle(pts[4], pts[5], 30f, borderPaint)
                borderPaint.pathEffect = oldEffect
            }
        }

        canvas.restore()
    }

    fun setBackgroundBitmap(bitmap: Bitmap) {
        backgroundImage = bitmap
        calculateLayout()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateLayout()
    }

    private fun calculateLayout() {
        val bitmap = backgroundImage ?: return
        if (width <= 0 || height <= 0) return

        val targetMaxWidth = width.toFloat()
        val targetMaxHeight = height * 0.9f

        val scaleFactor = min(targetMaxWidth / bitmap.width, targetMaxHeight / bitmap.height)
        val targetWidth = (bitmap.width * scaleFactor).toInt()
        val targetHeight = (bitmap.height * scaleFactor).toInt()

        scaledBackground = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)

        val left = (width - targetWidth) / 2f
        val top = (height - targetHeight) / 2f
        imageRect.set(left, top, left + targetWidth, top + targetHeight)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mode = NONE
                var hitSticker = false

                for (i in stickers.size - 1 downTo 0) {
                    val sticker = stickers[i]
                    if (sticker.containsPoint(x, y)) {
                        selectedSticker = sticker
                        stickers.forEach { it.isSelected = false }
                        sticker.isSelected = true

                        val pts = sticker.getMappedPoints()
                        if (distance(x, y, pts[0], pts[1]) < 80f) {
                            stickers.removeAt(i)
                            selectedSticker = null
                            invalidate()
                            return true
                        }

                        mode = if (distance(x, y, pts[4], pts[5]) < 80f) {
                            RESIZE_ROTATE
                        } else {
                            DRAG
                        }
                        hitSticker = true
                        break
                    }
                }

                if (!hitSticker) {
                    selectedSticker = null
                    stickers.forEach { it.isSelected = false }
                }

                lastX = x
                lastY = y
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                selectedSticker?.let { sticker ->
                    val pts = sticker.getMappedPoints()
                    val centerX = (pts[0] + pts[4]) / 2f
                    val centerY = (pts[1] + pts[5]) / 2f

                    if (mode == DRAG) {
                        sticker.matrix.postTranslate(x - lastX, y - lastY)
                    } else if (mode == RESIZE_ROTATE) {
                        val prevDist = hypot(lastX - centerX, lastY - centerY)
                        val currDist = hypot(x - centerX, y - centerY)
                        if (prevDist > 0) {
                            val scale = currDist / prevDist
                            sticker.matrix.postScale(scale, scale, centerX, centerY)
                        }

                        val prevAngle = atan2(lastY - centerY, lastX - centerX)
                        val currAngle = atan2(y - centerY, x - centerX)
                        val rotation = Math.toDegrees((currAngle - prevAngle).toDouble()).toFloat()
                        sticker.matrix.postRotate(rotation, centerX, centerY)
                    }

                    lastX = x
                    lastY = y
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP -> {
                mode = NONE
            }
        }
        return true
    }

    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return hypot(x1 - x2, y1 - y2)
    }
}