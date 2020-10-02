package com.example.mynext.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object ContextHelper {

    fun hideSoftKeyboard (view: View, context: Context?) {
        view.let {//hide keyboard after category inserted
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}