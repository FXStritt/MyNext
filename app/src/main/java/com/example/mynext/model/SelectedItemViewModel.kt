package com.example.mynext.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SelectedItemViewModel : ViewModel() {
    var selected = MutableLiveData<Item>()

    fun select(item: Item?) {
        selected.value = item
    }
}