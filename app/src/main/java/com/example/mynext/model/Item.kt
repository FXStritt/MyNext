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
                 @ColumnInfo val imageName: String,
                 @ColumnInfo val categoryId: String,
                 @ColumnInfo val dateCreated: Date = Date(), //not using newer LocalDate to retain lower API compatibility < 26
                 @ColumnInfo var done : Boolean = false
) {
    @PrimaryKey(autoGenerate = true) var itemId: Int = 0
}