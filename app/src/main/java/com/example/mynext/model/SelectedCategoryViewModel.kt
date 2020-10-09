package com.example.mynext.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SelectedCategoryViewModel : ViewModel() {
    val selected = MutableLiveData<Category>()

    fun select(category: Category) {
        selected.value = category
    }
}