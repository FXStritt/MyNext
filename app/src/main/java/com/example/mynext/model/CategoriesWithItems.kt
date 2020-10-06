package com.example.mynext.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class CategoriesWithItems (
    @Embedded val category: Category,
    @Relation(
        parentColumn = "title",
        entityColumn = "categoryId"
    )
    val items: List<Item>
)