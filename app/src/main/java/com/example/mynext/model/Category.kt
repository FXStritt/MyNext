package com.example.mynext.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mynext.util.RoomHelper

@Entity(tableName = RoomHelper.categoryTable)
data class Category(
    @PrimaryKey @ColumnInfo val title: String,
    @ColumnInfo val itemsName: String,
    @ColumnInfo val verb: String,
    @ColumnInfo val imageName: String = "" )
