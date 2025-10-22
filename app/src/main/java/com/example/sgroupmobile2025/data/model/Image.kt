package com.example.sgroupmobile2025.data.model

import android.net.Uri

data class Image (
    var imgSrc: Uri,
    var name: String,
    var date: Long,
    var isFavorite: Boolean
)