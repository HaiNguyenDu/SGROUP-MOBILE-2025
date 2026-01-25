package com.example.canvas.utils

import android.content.res.Resources

fun Float.dp(): Float =
    this * Resources.getSystem().displayMetrics.density