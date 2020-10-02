package com.example.mynext.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageHelper {

    const val CHOOSE_IMAGE_REQUEST_CODE = 200

    fun getImageIntent(): Intent {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        //Android will find the best app to fulfill this intent
        return Intent.createChooser(intent, "Choose image")
    }

    fun getBitmapFromUri(uri: Uri, currentActivity: Activity): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT < 28) {
                //deprecated in API 29
                MediaStore.Images.Media.getBitmap(currentActivity.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(currentActivity.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: IOException) {
            null
        }
    }

    fun saveBitmapToFileSystem(context: Context, filename: String, bitmap: Bitmap) {
        val cw = ContextWrapper(context)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val file = File(directory, "$filename.png")

        if (file.exists()) {
            file.delete()
        }

        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, it)
            it.flush()
            Log.d("MYTAG", "FileOutputStream flushed $file to $directory")
        }

    }

    fun retrieveBitmapFromFileSystem(context: Context, filename: String): Bitmap {
        val cw = ContextWrapper(context)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val file = File(directory, "$filename.png")

        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            DummyDataProvider(context).getDummyBitmap("BrokenLink")
        }
    }

}