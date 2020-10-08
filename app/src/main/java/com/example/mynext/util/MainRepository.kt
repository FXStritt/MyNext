package com.example.mynext.util

import androidx.lifecycle.LiveData
import com.example.mynext.data.Dao
import com.example.mynext.model.CategoriesWithItems
import com.example.mynext.model.Category
import com.example.mynext.model.Item

class MainRepository (private val dao: Dao) {
    val allCategories: LiveData<List<Category>> = dao.getAllCategories()
    val allItems: LiveData<List<Item>> = dao.getAllItems()
    val allCategoriesWithItems : LiveData<List<CategoriesWithItems>> = dao.getCategoriesWithItems()

    suspend fun insert(category: Category) {
        dao.insertCategory(category)
    }

    suspend fun insert(item: Item) {
        dao.insertItem(item)
    }

    suspend fun delete(item: Item) {
        dao.deleteItem(item)
    }

    suspend fun deleteAll() {
        dao.deleteTableItem()
        dao.deleteTableCategories()
    }
}