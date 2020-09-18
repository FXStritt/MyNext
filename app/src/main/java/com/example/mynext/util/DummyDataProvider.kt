package com.example.mynext.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.mynext.R
import com.example.mynext.model.Category
import com.example.mynext.model.Item

class DummyDataProvider(val context: Context?) {

    fun getDummyCategories(): List<Category> {
        return listOf(
            Category("Books"),
            Category("Movies"),
            Category("Places"),
            Category("Recipes")
        )
    }

    fun getDummyBooks(): List<Item> {
        val booksCategory = Category("Books")
        return listOf(
            Item(
                "Clean Code",
                "Book with rules to follow in order to write clean, scalable and testable code",
                "Oswaldo",
                getDummyBitmap(booksCategory),
                booksCategory
            ),
            Item(
                "Sfumato",
                "Not sure what this book is about, something related to art and what the meaning of life is",
                "Sacha",
                getDummyBitmap(booksCategory),
                booksCategory
            ),
            Item(
                "Oryx and Crake",
                "One of the best read of the 21st century... apparently",
                "bestbooks.com",
                getDummyBitmap(booksCategory),
                booksCategory
            ),
            Item(
                "Out of the head and into the heart",
                "Motivational book that I found in the stoss hotel I stayed at.",
                "Stoss",
                getDummyBitmap(booksCategory),
                booksCategory
            )

        )
    }

    fun getDummyMovies(): List<Item> {
        val moviesCategory = Category("Movies")
        return listOf(
            Item(
                "Tenet",
                "New christoher Nolan movie",
                "NA",
                getDummyBitmap(moviesCategory),
                moviesCategory
            ),

            Item(
                "Le chant du loup",
                "Some movie abour french submarines that this person I spoke to on the train recommended",
                "Stranger",
                getDummyBitmap(moviesCategory),
                moviesCategory),

            Item(
                "Stalker",
                "Macha told me this was a must see russian move, by tarkovski",
                "Macha",
                getDummyBitmap(moviesCategory),
                moviesCategory)
        )
    }

    fun getDummyPlaces(): List<Item> {
        val placesCategory = Category("Places")
        return listOf(
            Item(
                "London",
                "I always wanted to go to London",
                "Myself",
                getDummyBitmap(placesCategory),
                placesCategory
            ),
            Item(
                "Back&Buc Creperie",
                "I was told they do really good food for quite cheap",
                "John",
                getDummyBitmap(placesCategory),
                placesCategory
            ),
            Item(
                "Pripyat",
                "Man, there are some creepy stories about this place. Would be cool to visit",
                "Dark Tourist",
                getDummyBitmap(placesCategory),
                placesCategory
            )

        )
    }

    fun getDummyBitmap(category: Category): Bitmap {
        return when (category.title) {
            "Books" -> BitmapFactory.decodeResource(context?.resources, R.drawable.book)
            "Movies" -> BitmapFactory.decodeResource(context?.resources, R.drawable.movies)
            else -> BitmapFactory.decodeResource(context?.resources, R.drawable.places)
        }
    }
}