package com.example.mynext.model

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mynext.data.MainRoomDB
import com.example.mynext.util.ImageHelper
import com.example.mynext.util.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MainRepository
    val allItems: LiveData<List<Item>>

    init {
        val dao = MainRoomDB.getDatabase(application).dao()
        repository = MainRepository(dao)
        allItems = repository.allItems
    }

    fun insert(item: Item, bitmap: Bitmap) = viewModelScope.launch (Dispatchers.IO) {
        repository.insert(item)
        ImageHelper.saveBitmapToFileSystem(getApplication(),item.imageName, bitmap)
    }

    fun deleteItem(item: Item, categoryImageName : String?) = viewModelScope.launch (Dispatchers.IO) {
        repository.delete(item)
        ImageHelper.deleteItemBitmapFromFileSystem(getApplication(), item.imageName, categoryImageName)
    }

    fun updateItem(item: Item) = viewModelScope.launch (Dispatchers.IO) {
        repository.update(item)
    }
}
