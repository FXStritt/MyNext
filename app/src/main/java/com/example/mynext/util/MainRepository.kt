package com.example.mynext.util

import android.content.Context
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
        dao.delete(item)
    }

    suspend fun deleteAll() {
        dao.deleteTableItem()
        dao.deleteTableCategories()
    }

    suspend fun populateDummyItems(context: Context) {
        for (item : Item in DummyDataProvider(context).getDummyItemsList()) {
            dao.insertItem(item)
        }
    }

    suspend fun update(item: Item) {
        dao.update(item)
    }
}