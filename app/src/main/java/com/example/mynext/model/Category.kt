package com.example.mynext.model

import android.graphics.Bitmap

data class Category (val title: String, val itemsName: String, val verb: String, var defaultItemsImage: Bitmap? = null)
//TODO mutable and nullable bitmap is temporary, until Room database is created and we can move away from DummyDataProvider