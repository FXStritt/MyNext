package com.example.mynext.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.mynext.model.CategoriesWithItems
import com.example.mynext.model.Category
import com.example.mynext.model.Item
import com.example.mynext.util.RoomHelper

@Dao
interface Dao {

   @Query("DELETE FROM ${RoomHelper.itemTable}" )
   suspend fun deleteTableItem()
   @Query("DELETE FROM ${RoomHelper.categoryTable}" )
   suspend fun deleteTableCategories()

   @Query("SELECT * FROM ${RoomHelper.categoryTable}")
   fun getAllCategories(): LiveData<List<Category>>

   @Query("SELECT * FROM ${RoomHelper.itemTable}")
   fun getAllItems(): LiveData<List<Item>>

   @Transaction
   @Query("SELECT * FROM category_table")
   fun getCategoriesWithItems() : LiveData<List<CategoriesWithItems>>

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   suspend fun insertCategory(category: Category)

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertItem(item: Item)

   @Delete
   suspend fun deleteItem(item: Item)
}