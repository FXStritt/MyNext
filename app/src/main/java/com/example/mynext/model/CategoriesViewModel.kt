package com.example.mynext.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mynext.data.MainRoomDB
import com.example.mynext.util.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MainRepository
    val allCategories: LiveData<List<Category>>

    init {
        val dao = MainRoomDB.getDatabase(application).dao()
        repository = MainRepository(dao)
        allCategories = repository.allCategories
    }

    //launches new coroutine to dispatch data in non-blocking way
    fun insert(category: Category) = viewModelScope.launch (Dispatchers.IO) {
        repository.insert(category)
    }
}