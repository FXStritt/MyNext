package com.example.mynext.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.mynext.R
import com.example.mynext.model.Category

class DummyDataProvider(val context: Context?) {

    fun getDummyCategoriesWithBitmap(): List<Pair<Category,Bitmap>> {
        return listOf(
            Pair(
                Category("Books", "Book", "Read", "dummyBooks"),
                getDummyBitmap("Books")),
            Pair(
                Category("Movies", "Movie", "Watch", "dummyMovies"),
                getDummyBitmap("Movies")),
            Pair(
                Category("Places", "Place", "Visit", "dummyPlaces"),
                getDummyBitmap("Places"))
        )
    }

    fun getDummyBitmap(title: String?): Bitmap {
        return when (title) {
            "BrokenLink" -> BitmapFactory.decodeResource(context?.resources, R.drawable.ic_baseline_broken_image_24)
            "Books" -> BitmapFactory.decodeResource(context?.resources, R.drawable.book)
            "Movies" -> BitmapFactory.decodeResource(context?.resources, R.drawable.movies)
            else -> BitmapFactory.decodeResource(context?.resources, R.drawable.places)
        }
    }
}