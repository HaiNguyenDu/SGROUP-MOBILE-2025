package com.example.canvas.data

import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.ColorInt
class PaintManager {

    private val pencil = PencilTool()
    private val pen = PenTool()
    private val highlighter = HighlighterTool()
    private val crayon = CrayonTool()
    private val eraser = EraserTool()

    var currentTool: PaintTool = pen
        private set

    fun selectPaint(type: PenType) {
        currentTool = when (type) {
            PenType.PENCIL -> pencil
            PenType.PEN -> pen
            PenType.HIGHLIGHTER -> highlighter
            PenType.CRAYON -> crayon
            PenType.ERASER -> eraser
        }
    }

    fun applyToPaint(paint: Paint): Paint {
        paint.reset()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.xfermode = null

        currentTool.applyToPaint(paint)
        return paint
    }

    fun changeColor(color: Int) {
        currentTool.color = color
    }

    fun changeStroke(stroke: Float) {
        currentTool.baseStroke = stroke
    }

    fun changeAlpha(alpha: Int) {
        currentTool.alpha = alpha
    }
}
