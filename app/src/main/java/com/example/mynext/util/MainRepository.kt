package com.example.mynext.util

import androidx.lifecycle.LiveData
import com.example.mynext.data.Dao
import com.example.mynext.model.Category
import com.example.mynext.model.Item

class MainRepository (private val dao: Dao) {
    val allCategories: LiveData<List<Category>> = dao.getAllCategories()
    val allItems: LiveData<List<Item>> = dao.getAllItems()

    suspend fun insert(category: Category) {
        dao.insertCategory(category)
    }

    suspend fun insert(item: Item) {
        dao.insertItem(item)
    }
}