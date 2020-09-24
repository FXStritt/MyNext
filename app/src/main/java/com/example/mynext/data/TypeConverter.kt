package com.example.mynext.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.util.*

object TypeConverter {

    @TypeConverter
    @JvmStatic //for room to use as regular static functions
    fun timestampToDate(value: Long) : Date {
        return Date(value)
    }
    @TypeConverter
    @JvmStatic
    fun dateToTimestamp(date: Date) : Long {
        return date.time
    }

    @TypeConverter
    @JvmStatic
    fun bitmapToByteArray(bitmap: Bitmap) : ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
        return stream.toByteArray()
    }

    @TypeConverter
    @JvmStatic
    fun byteArrayToBitmap(array: ByteArray) : Bitmap {
        return BitmapFactory.decodeByteArray(array,0,array.size)
    }

}