package com.example.mynext.model

import android.graphics.Bitmap
import java.util.*

data class Item (val title: String, val description: String,
                 val recommender: String, val image: Bitmap,
                 val category: Category, val dateCreated: Date = Date())