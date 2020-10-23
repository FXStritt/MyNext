package com.example.mynext.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.ZoneOffset

object TypeConverter {

    @TypeConverter
    @JvmStatic //for room to use as regular static functions
    fun timestampToDate(value: Long) : LocalDateTime {
        return LocalDateTime.ofEpochSecond(value,0, ZoneOffset.UTC)
    }
    @TypeConverter
    @JvmStatic
    fun dateToTimestamp(date: LocalDateTime) : Long {
        return date.toEpochSecond(ZoneOffset.UTC)
    }

    @TypeConverter
    @JvmStatic
    fun bitmapToByteArray(bitmap: Bitmap) : ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,10,stream)
        return stream.toByteArray()
    }

    @TypeConverter
    @JvmStatic
    fun byteArrayToBitmap(array: ByteArray) : Bitmap {
        return BitmapFactory.decodeByteArray(array,0,array.size)
    }

}