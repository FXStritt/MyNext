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
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object ImageHelper {

    const val CHOOSE_IMAGE_REQUEST_CODE = 200
    private const val TEMP_FILE_NAME = "temp_file"

    @Throws(IOException::class)
    private fun createTempImageFile(context: Context): File {
        // Create an image file name
        val storageDir: File = context.filesDir
        return File(storageDir, "$TEMP_FILE_NAME.png")
    }

    fun deleteTempImageFileIfExist(context: Context): Boolean {
        return try {
            val storageDir = context.filesDir
            val fileToDelete = File(storageDir, "$TEMP_FILE_NAME.png")
            if (fileToDelete.exists()) {
                fileToDelete.delete()
            }
            true
        } catch (e: IOException) {
            false
        }
    }

    fun getImageIntent(context: Context): Intent {

        //TODO manage permissions & request camera access to user
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val chooseImageIntent = Intent(Intent.ACTION_GET_CONTENT)
        chooseImageIntent.type = "image/*"

        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Choose image")
        chooserIntent.putExtra(Intent.EXTRA_INTENT, chooseImageIntent)

        takePictureIntent.resolveActivity(context.packageManager)?.also {
            val photoFile: File? = try {
                createTempImageFile(context)
            } catch (ex: IOException) {
                null
            }

            photoFile?.also {

                val photoFileUri = FileProvider.getUriForFile(
                    context,
                    "com.example.mynext.fileprovider",
                    it
                )

                //If there is no activity to capture a picture or there was an error creating a tempfile to store the taken picture,
                //then this code will not be executed and the chooser will not contain the option to take a camera picture
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri)
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(takePictureIntent))
            }
        }

        return chooserIntent
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

    //Image taken by camera is always stored in same directory under same filename
    fun getImageTakenByCamera(currentActivity: Activity): Bitmap? {
        val storageDir: File = currentActivity.filesDir
        val file = File(storageDir, "$TEMP_FILE_NAME.png")
        return getBitmapFromUri(file.toUri(), currentActivity)
    }

    fun saveBitmapToFileSystem(context: Context, filename: String, bitmap: Bitmap) {
        val file = getFileRef(context, filename)

        if (!file.exists()) {

            val resizedBitmap = resizeBitmapToMax480by480(bitmap)

            FileOutputStream(file).use {
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, it)
                it.flush()
            }
        }
    }

    private fun resizeBitmapToMax480by480(bitmap: Bitmap): Bitmap {
        with(bitmap) {
            return if (width > 480 || height > 480) {
                val aspectRatio = width.toDouble() / height.toDouble()
                val newWidth: Int
                val newHeight: Int
                if (width > height) {
                    newWidth = 480
                    newHeight = (newWidth / aspectRatio).toInt()
                } else {
                    newHeight = 480
                    newWidth = (aspectRatio * newHeight).toInt()
                }
                Bitmap.createScaledBitmap(this, newWidth, newHeight, false)
            } else {
                this
            }
        }
    }

    fun retrieveBitmapFromFileSystem(context: Context, filename: String): Bitmap {
        val file = getFileRef(context, filename)

        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            DummyDataProvider(context).getDummyBitmap("BrokenLink")
        }
    }

    fun deleteItemBitmapFromFileSystem(
        context: Context,
        filename: String,
        categoryImageName: String?
    ) {
        if (filename != categoryImageName) { //we do not want to delete the image if it is a category dummy image as this would impact other items.
            val file = getFileRef(context, filename)
            if (file.exists()) {
                file.delete()
            }
        }
    }

    private fun getFileRef(context: Context, filename: String): File {
        val cw = ContextWrapper(context)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        return File(directory, "$filename.png")
    }
}