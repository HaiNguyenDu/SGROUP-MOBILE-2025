package com.example.editphoto.contants

import androidx.camera.core.AspectRatio

object Contants {
    const val NONE = 0
    const val DRAG = 1
    const val RESIZE_ROTATE = 2
    val REQUIRED_PERMISSIONS = arrayOf(
        android.Manifest.permission.CAMERA
    )
}
enum class RatioSize(val ratio: Double) {
    RATIO_1_1(1.0),
    RATIO_4_3(3.0 / 4.0),
    RATIO_16_9(9.0 / 16.0),
}