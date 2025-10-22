package com.example.sgroupmobile2025.data.local

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.example.sgroupmobile2025.data.model.Image

class LocalData(private var context: Context) {
    suspend fun getImages(): List<Image>{
        val images = mutableListOf<Image>()
        val uriStore = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.IS_PRIVATE,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        val selection = null
        val selectionArgs = null
        try{
            context.contentResolver.query(
                uriStore,
                projection,
                selection,
                selectionArgs,
                "${MediaStore.Images.Media.DATE_ADDED} DESC"
            )?.use {
                cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
//                val favoriteColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.IS_FAVORITE)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                while (cursor.moveToNext()){
                    val id = cursor.getLong(idColumn)
                    val date = cursor.getLong(dateColumn)
//                    val isFavorite = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                        cursor.getInt(cursor.getColumnIndexOrThrow(favoriteColumn)) == 1
//                    } else false
                    val name = cursor.getString(nameColumn)
                    val imgSrc = ContentUris.withAppendedId(uriStore, id)
                    images.add(Image( imgSrc, name, date, false))
                }
            }
        }catch(e: Exception){
            e.printStackTrace()
        }
        return images
    }

}