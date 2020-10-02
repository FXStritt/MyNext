package com.example.mynext.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mynext.model.Category
import com.example.mynext.model.Item
import com.example.mynext.util.RoomHelper

@Dao
interface Dao {

   @Query("DELETE FROM ${RoomHelper.itemTable}" )
   suspend fun deleteTableItem()
   @Query("DELETE FROM ${RoomHelper.categoryTable}" )
   suspend fun deleteTableCategories()

   @Query("SELECT * from ${RoomHelper.categoryTable}")
   fun getAllCategories(): LiveData<List<Category>>

   @Query("SELECT * from ${RoomHelper.itemTable}")
   fun getAllItems(): LiveData<List<Item>>

//   @Query("SELECT * from ${RoomHelper.itemTable} WHERE categoryId = :categoryID")
//   fun getItemsFromCategory(categoryID: String): LiveData<List<Item>>

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   suspend fun insertCategory(category: Category)

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertItem(item: Item)
}