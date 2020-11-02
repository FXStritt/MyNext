package com.example.mynext.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mynext.util.RoomNames
import java.time.LocalDateTime

@Entity(tableName = RoomNames.itemTable)
data class Item(
    @ColumnInfo val itemTitle: String,
    @ColumnInfo val description: String,
    @ColumnInfo val recommender: String,
    @ColumnInfo var imageName: String,
    @ColumnInfo val categoryId: String,
    @ColumnInfo var dateCreated: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo var done: Boolean = false,
    @ColumnInfo var dateDone : LocalDateTime = LocalDateTime.now(), //Boolean "done" will determine if we take this date into account, so default value is OK
) {
    @PrimaryKey(autoGenerate = true)
    var itemId: Int = 0
}