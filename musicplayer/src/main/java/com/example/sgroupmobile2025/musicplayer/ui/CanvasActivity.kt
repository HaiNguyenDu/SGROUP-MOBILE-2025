package com.example.sgroupmobile2025.musicplayer.ui

import android.graphics.*
import android.os.Bundle
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.sgroupmobile2025.musicplayer.R

class CanvasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(CanvasView(this))
    }
}

class CanvasView(context: android.content.Context) : View(context) {

    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        strokeWidth = 8f
        style = Paint.Style.STROKE
    }

    private val textPaint = TextPaint().apply {
        isAntiAlias = true
        color = Color.BLUE
        textSize = 45f
    }

    private val bitmap by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.album)
    }

    private val curvePath = Path().apply {
        moveTo(400f, 800f)
        quadTo(400f, 600f, 700f, 800f)
    }

    private val myPicture = Picture().apply {
        val c = beginRecording(200, 200)
        val p = Paint().apply { color = Color.BLUE; style = Paint.Style.FILL }
        c.drawCircle(100f, 100f, 50f, p)
        endRecording()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(Color.parseColor("#F0F0F0")) // màu xám

        // hình học
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE

        // line
        canvas.drawLine(100f, 150f, 400f, 150f, paint)

        // circle
        canvas.drawCircle(200f, 350f, 100f, paint)

        // rectangle
        canvas.drawRect(500f, 250f, 850f, 450f, paint)

        // Arc
        paint.color = Color.parseColor("#FF9800")
        canvas.drawArc(RectF(100f, 500f, 350f, 700f), 0f, 180f, true, paint)

        // bitmap
        canvas.drawBitmap(bitmap, 450f, 1500f, null)

        // path và text
        paint.color = Color.GREEN
        canvas.drawPath(curvePath, paint)

        textPaint.color = Color.YELLOW
        textPaint.textSize = 40f
        canvas.drawTextOnPath("TEXT CHẠY THEO ĐƯỜNG CONG", curvePath, 50f, -20f, textPaint)

        // text
        textPaint.color = Color.BLACK
        textPaint.textSize = 60f
        canvas.drawText("Hello TP", 100f, 950f, textPaint)

        // staticLayout
        textPaint.color = Color.DKGRAY
        textPaint.textSize = 60f
        val textContent = "Văn bản tự động xuống dòng khi chạm vào lề của chiều rộng"

        val layout = StaticLayout.Builder
            .obtain(textContent, 0, textContent.length, textPaint, width - 200)
            .build()

        canvas.save()
        canvas.translate(100f, 1050f)
        layout.draw(canvas)
        canvas.restore()


        //  drawPoints
        paint.strokeWidth = 20f
        paint.color = Color.MAGENTA
        val points = floatArrayOf(500f, 550f, 600f, 550f, 700f, 550f)
        canvas.drawPoints(points, paint)

        //  drawLines
        paint.strokeWidth = 8f
        paint.color = Color.BLACK
        val lines = floatArrayOf(500f, 500f, 900f, 500f, 500f, 520f, 900f, 520f)
        canvas.drawLines(lines, paint)

        //  drawRoundRect
        paint.color = Color.CYAN
        canvas.drawRoundRect(400f, 850f, 650f, 930f, 20f, 20f, paint)

        // drawOval
        paint.color = Color.LTGRAY
        canvas.drawOval(700f, 850f, 950f, 930f, paint)

        // drawPosText
        textPaint.textSize = 40f
        @Suppress("DEPRECATION")
        canvas.drawPosText("ABC", floatArrayOf(750f, 550f, 800f, 580f, 850f, 610f), textPaint)

        // drawPicture
        canvas.save()
        canvas.translate(800f, 1200f)
        canvas.drawPicture(myPicture)
        canvas.restore()

        //  drawVertices
        paint.style = Paint.Style.FILL
        val verts = floatArrayOf(100f, 1400f, 300f, 1400f, 200f, 1600f)
        val vertColors = intArrayOf(Color.RED, Color.GREEN, Color.BLUE)
        canvas.drawVertices(Canvas.VertexMode.TRIANGLES, verts.size, verts, 0,
            null, 0, vertColors, 0, null, 0, 0, paint)
    }
}