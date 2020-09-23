package com.example.mynext.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mynext.model.Category
import com.example.mynext.util.RoomHelper

@Dao
interface CategoryDao {

   @Query("SELECT * from ${RoomHelper.categoryTable}")
   fun getAllCategories(): List<Category>

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   suspend fun insertCategory(category: Category)
}