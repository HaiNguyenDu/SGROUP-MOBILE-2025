package com.example.canvas.data

import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode

enum class PenType {
    PEN,
    PENCIL,
    ERASER,
    CRAYON,
    HIGHLIGHTER
};

interface PaintTool {
    val type: PenType
    var color: Int
    var baseStroke: Float
    var alpha: Int

    fun applyToPaint(paint: Paint)
}

class PenTool : PaintTool {

    override val type = PenType.PEN
    override var color: Int = Color.BLACK
    override var baseStroke: Float = 8f
    override var alpha: Int = 255

    override fun applyToPaint(paint: Paint) {
        paint.color = color
        paint.alpha = alpha
        paint.strokeWidth = baseStroke
    }
}

class PencilTool : PaintTool {

    override val type = PenType.PENCIL
    override var color: Int = Color.BLACK
    override var baseStroke: Float = 6f
    override var alpha: Int = 180

    override fun applyToPaint(paint: Paint) {
        paint.color = color
        paint.alpha = alpha
        paint.strokeWidth = baseStroke
        paint.pathEffect = CornerPathEffect(8f)
    }
}
class HighlighterTool : PaintTool {

    override val type = PenType.HIGHLIGHTER
    override var color: Int = Color.YELLOW
    override var baseStroke: Float = 30f
    override var alpha: Int = 80

    override fun applyToPaint(paint: Paint) {
        paint.color = color
        paint.alpha = alpha
        paint.strokeWidth = baseStroke
    }
}
class CrayonTool : PaintTool {

    override val type = PenType.CRAYON
    override var color: Int = Color.BLACK
    override var baseStroke: Float = 14f
    override var alpha: Int = 200

    override fun applyToPaint(paint: Paint) {
        paint.color = color
        paint.alpha = alpha
        paint.strokeWidth = baseStroke
        paint.pathEffect = DashPathEffect(floatArrayOf(12f, 8f), 0f)
    }
}
class EraserTool : PaintTool {

    override val type = PenType.ERASER
    override var color: Int = Color.TRANSPARENT
    override var baseStroke: Float = 40f
    override var alpha: Int = 255

    override fun applyToPaint(paint: Paint) {
        paint.strokeWidth = baseStroke
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
}

