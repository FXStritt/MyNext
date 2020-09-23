package com.example.mynext.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mynext.util.RoomHelper

@Entity(tableName = RoomHelper.categoryTable)
data class Category (@PrimaryKey val title: String,
                     @ColumnInfo val itemsName: String,
                     @ColumnInfo val verb: String,
                     @ColumnInfo var defaultItemsImage: Bitmap? = null)

//TODO mutable and nullable bitmap is temporary, until Room database is created and we can move away from DummyDataProvider