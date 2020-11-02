package com.example.mynext.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SelectedItemViewModel : ViewModel() {
    var selected = MutableLiveData<Item>()
    var imageWasChanged = false

    fun select(item: Item?) {
        selected.value = item
        imageWasChanged = false
    }

    fun imageWasChanged() {
        imageWasChanged = true
    }
}