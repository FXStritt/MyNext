package com.example.mynext.model

import androidx.room.Embedded
import androidx.room.Relation
import kotlin.reflect.KClass

//data class CategoryWithItems (
//    @Embedded val category: Category,
//            @Relation (
//                parentColumn = "title",
//                entity = "categoryId"
//            )
//            val items: List<Item>
//)