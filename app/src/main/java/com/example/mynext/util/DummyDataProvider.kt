package com.example.mynext.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.mynext.R
import com.example.mynext.model.Category
import com.example.mynext.model.Item
import java.util.*

class DummyDataProvider(val context: Context?) {

    fun getDummyItemsList(): List<Item> {
        return mutableListOf(
            Item("Movie 1", "great movie", "John", "dummyMovies", "Movies", Date()),
            Item("Movie 2", "bad movie", "Mike", "dummyMovies", "Movies", Date()),
            Item("Book 1", "great book", "John", "dummyBooks", "Books", Date()),
            Item("Book 2", "bad book", "Mike", "dummyBooks", "Books", Date()),
            Item("Place 1", "great place", "Mike", "dummyPlaces", "Places", Date())
        )
    }

    fun getDummyCategoriesWithBitmap(): List<Pair<Category, Bitmap>> {
        return listOf(
            Pair(
                Category("Books", "Book", "Read", "dummyBooks"),
                getDummyBitmap("Books")
            ),
            Pair(
                Category("Movies", "Movie", "Watch", "dummyMovies"),
                getDummyBitmap("Movies")
            ),
            Pair(
                Category("Places", "Place", "Visit", "dummyPlaces"),
                getDummyBitmap("Places")
            )
        )
    }

    fun getDummyBitmap(title: String?): Bitmap {
        return when (title) {
            "BrokenLink" -> BitmapFactory.decodeResource(
                context?.resources,
                R.drawable.baseline_broken_image_black_24dp
            )
            "Books" -> BitmapFactory.decodeResource(context?.resources, R.drawable.book)
            "Movies" -> BitmapFactory.decodeResource(context?.resources, R.drawable.movies)
            else -> BitmapFactory.decodeResource(context?.resources, R.drawable.places)
        }
    }
}