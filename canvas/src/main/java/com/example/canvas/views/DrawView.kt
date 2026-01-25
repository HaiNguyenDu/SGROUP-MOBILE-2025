package com.example.canvas.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import com.example.canvas.data.PaintManager
import com.example.canvas.data.PenType
import com.example.canvas.data.Stroke
import kotlin.math.min


class DrawView(context: Context, atr: AttributeSet): View(context, atr) {
    private val paintToolManager = PaintManager()
    private var currentPaint = Paint()
    private var currentPath = Path()
    private lateinit var bitmap: Bitmap
    private var baseImage: Bitmap? = null
    private var imageLeft = 0f
    private var imageTop = 0f
    private var imageRight = 0f
    private var imageBottom = 0f

    private var backgroundColor = Color.WHITE
    private val brightnessPaint = Paint()
    private var brightness = 0f
    private val undoStrokes = mutableListOf<Stroke>()
    private val redoStrokes = mutableListOf<Stroke>()
    private var listener: ((Int,Int) -> Unit)? = null
    private lateinit var bufferCanvas: Canvas

    init {
        currentPaint = paintToolManager.applyToPaint(currentPaint)
    }

    fun setListener(listener: (Int, Int) -> Unit ){
        this.listener = listener
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bufferCanvas = Canvas(bitmap)
        reBuildBitmap(backgroundColor)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        listener?.invoke(redoStrokes.size, undoStrokes.size)
        canvas.drawBitmap(bitmap, 0f, 0f,brightnessPaint)
        if (paintToolManager.currentTool.type != PenType.ERASER) {
            canvas.drawPath(currentPath, currentPaint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event == null) return false
        val x = event.x
        val y = event.y
        if(x < 0 || x > width || y < 0 || y > height) return false
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                currentPath.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                val stroke = Stroke(Path(currentPath), Paint(currentPaint))
                undoStrokes.add(stroke)
                bufferCanvas.drawPath(currentPath, currentPaint)
                currentPath.reset()
                redoStrokes.clear()
            }
        }
        invalidate()
        return true
    }
    fun clearCanvas(){
        baseImage = null
        bitmap.eraseColor(Color.TRANSPARENT)
        imageLeft = 0f
        imageTop = 0f
        imageRight = width.toFloat()
        imageBottom = height.toFloat()
    }
    fun setImageBitmap(bitmap: Bitmap) {
        clearCanvas()
        baseImage = bitmap
        Log.e("dataaaaa", "den day")

        val scaleX = width.toFloat() / bitmap.width
        val scaleY = height.toFloat() / bitmap.height
        val scaleFactor = min(scaleX, scaleY)

        val scaledBitmap = bitmap.scale(
            (bitmap.width * scaleFactor).toInt(),
            (bitmap.height * scaleFactor).toInt()
        )

        val left = (width - scaledBitmap.width) / 2f
        val top = (height - scaledBitmap.height) / 2f

        imageLeft = left
        imageTop = top
        imageRight = left + scaledBitmap.width
        imageBottom = top + scaledBitmap.height

        this.bitmap.eraseColor(Color.TRANSPARENT)
        bufferCanvas.drawBitmap(scaledBitmap, left, top, null)

        invalidate()
    }

    fun undo(){
        if(undoStrokes.isEmpty()) return
        val last = undoStrokes.removeAt(undoStrokes.lastIndex)
        redoStrokes.add(last)
        reBuildBitmap(backgroundColor)
    }
    fun reBuildBitmap(backgroundColor: Int){
        bitmap.eraseColor(backgroundColor)
        baseImage?.let {
            val scaleX = width.toFloat() / it.width
            val scaleY = height.toFloat() / it.height
            val scaleFactor = min(scaleX, scaleY)

            val scaledBitmap = it.scale(
                (it.width * scaleFactor).toInt(),
                (it.height * scaleFactor).toInt()
            )

            val left = (width - scaledBitmap.width) / 2f
            val top = (height - scaledBitmap.height) / 2f

            imageLeft = left
            imageTop = top
            imageRight = left + scaledBitmap.width
            imageBottom = top + scaledBitmap.height

            bufferCanvas.drawBitmap(scaledBitmap, left, top, null)
        }
        for(stroke in undoStrokes){
            bufferCanvas.drawPath(stroke.path, stroke.paint)
        }
        invalidate()
    }

    fun redo(){
        if(redoStrokes.isEmpty()) return
        val last = redoStrokes.removeAt(redoStrokes.lastIndex)
        bufferCanvas.drawPath(last.path, last.paint)
        undoStrokes.add(last)
        invalidate()
    }
    fun changePaintTool(type: PenType){
        paintToolManager.selectPaint(type)
        updatePaint()
    }
    fun updatePaint() {
        currentPaint.reset()
        currentPaint.isAntiAlias = true
        paintToolManager.applyToPaint(currentPaint)
    }

    fun changeColor(color: Int){
        paintToolManager.changeColor(color)
        updatePaint()
    }
    fun changeStroke(stroke: Float){
        paintToolManager.changeStroke(stroke)
        updatePaint()
    }
    fun changeAlpha(alpha: Int){
        paintToolManager.changeAlpha(alpha)
        updatePaint()
    }

    fun changeBackgroundColor(color: Int){
        backgroundColor = color
        reBuildBitmap(backgroundColor)
    }
    fun changeAlphaView(alpha: Int) {
        this.alpha = alpha / 255f
    }
    fun changeBrightness(value: Int) {
        // value: -255 .. 255
        brightness = value.toFloat()

        val matrix = ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, brightness,
                0f, 1f, 0f, 0f, brightness,
                0f, 0f, 1f, 0f, brightness,
                0f, 0f, 0f, 1f, 0f
            )
        )

        brightnessPaint.colorFilter = ColorMatrixColorFilter(matrix)
        invalidate()
    }
    fun getBitmapFromView(): Bitmap {
        val resultBitmap = createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val resultCanvas = Canvas(resultBitmap)
        resultCanvas.drawColor(Color.WHITE)
        resultCanvas.drawBitmap(bitmap, 0f, 0f, brightnessPaint)
        resultCanvas.drawPath(currentPath, currentPaint)
        return resultBitmap
    }
}
