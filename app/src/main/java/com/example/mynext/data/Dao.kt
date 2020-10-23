package com.example.mynext.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.mynext.model.CategoriesWithItems
import com.example.mynext.model.Category
import com.example.mynext.model.Item

@Dao
interface Dao {

   @Query("DELETE FROM item_table" )
   suspend fun deleteTableItem()
   @Query("DELETE FROM category_table" )
   suspend fun deleteTableCategories()

   @Query("SELECT * FROM category_table")
   fun getAllCategories(): LiveData<List<Category>>

   @Query("SELECT * FROM item_table ORDER BY done, CASE WHEN done=0 THEN dateCreated ELSE dateDone END DESC")
   fun getAllItems(): LiveData<List<Item>>

   @Transaction
   @Query("SELECT * FROM category_table")
   fun getCategoriesWithItems() : LiveData<List<CategoriesWithItems>>

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   suspend fun insertCategory(category: Category)

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertItem(item: Item)

   @Delete
   suspend fun delete(item: Item)

   @Update
   suspend fun update(item: Item)
}