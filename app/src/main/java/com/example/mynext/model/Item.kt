package com.example.mynext.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "item_table")
data class Item (@ColumnInfo val title: String,
                 @ColumnInfo val description: String,
                 @ColumnInfo val recommender: String,
                 @ColumnInfo val image: Bitmap,
                 @ColumnInfo val category: Category,
                 @ColumnInfo val dateCreated: Date = Date()
){
    @PrimaryKey(autoGenerate = true) val id: Int = 0
}