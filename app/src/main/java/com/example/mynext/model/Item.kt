package com.example.mynext.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mynext.util.RoomHelper
import java.util.*

@Entity(tableName = RoomHelper.itemTable)
data class Item (@ColumnInfo val title: String,
                 @ColumnInfo val description: String,
                 @ColumnInfo val recommender: String,
                 @ColumnInfo val image: Bitmap,
                 @ColumnInfo val categoryId: String,
                 @ColumnInfo val dateCreated: Date = Date()
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}