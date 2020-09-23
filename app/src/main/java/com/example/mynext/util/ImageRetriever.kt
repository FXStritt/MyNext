package com.example.mynext.util

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.IOException

object ImageRetriever {

    const val CHOOSE_IMAGE_REQUEST_CODE = 200

    fun getImageIntent() : Intent {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        //Android will find the best app to fulfill this intent
        return Intent.createChooser(intent, "Choose image")
    }

    fun getBitmapFromUri(uri: Uri, currentActivity : Activity) : Bitmap? {
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

}