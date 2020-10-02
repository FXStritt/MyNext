package com.example.mynext.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mynext.util.RoomHelper

@Entity(tableName = RoomHelper.categoryTable)
data class Category (@PrimaryKey @ColumnInfo val title: String,
                     @ColumnInfo val itemsName: String,
                     @ColumnInfo val verb: String,
//                     @ColumnInfo var defaultItemsImage: Bitmap? = null)
)//TODO implement images for categories

//TODO mutable and nullable bitmap is temporary, until Room database is created and we can move away from DummyDataProvider