package com.example.mynext.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mynext.util.RoomHelper
import java.util.*

@Entity(tableName = RoomHelper.itemTable)
data class Item (@ColumnInfo val itemTitle: String,
                 @ColumnInfo val description: String,
                 @ColumnInfo val recommender: String,
//                 @ColumnInfo val image: Bitmap, //TODO saving bitmap to db is causing problem in re-reading it. Save path instead
                 @ColumnInfo val categoryId: String,
                 @ColumnInfo val dateCreated: Date = Date()
) {
    @PrimaryKey(autoGenerate = true) var itemId: Int = 0
}