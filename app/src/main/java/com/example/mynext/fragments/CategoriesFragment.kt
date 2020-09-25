package com.example.mynext.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mynext.R
import com.example.mynext.model.CategoriesViewModel
import com.example.mynext.model.Category
import com.example.mynext.model.CategoryViewModel
import com.example.mynext.ui.CategoryAdapter
import kotlinx.android.synthetic.main.fragment_category.*

//TODO - on first load, if no DB detected, create dummy categories
class CategoriesFragment : Fragment(), CategoryClickListener {

    private val selectedCategory: CategoryViewModel by activityViewModels()
    private lateinit var categoryViewModel: CategoriesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        categoryViewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)

        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryAdapter = CategoryAdapter(this)

        val currentCategories = categoryViewModel.allCategories.value
        if (currentCategories != null) {
            categoryAdapter.setCategories(currentCategories)
        }

        categoryViewModel.allCategories.observe(viewLifecycleOwner) {
            categoryAdapter.setCategories(it)

            Log.d("MYTAG","Loaded ${it.size} categories")
        }

        categories_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity,2, GridLayoutManager.VERTICAL, false)
            adapter = categoryAdapter
        }
    }

    override fun onCategoryClickListener(category: Category) {
        selectedCategory.select(category)

        if (category.title == CategoryAdapter.ADD_CATEGORY) {
            findNavController().navigate(R.id.action_CategoriesFragment_to_NewCategoryFragment)
        } else {
            findNavController().navigate(R.id.action_CategoriesFragment_to_ItemsFragment)
        }
    }
}

interface CategoryClickListener {
    fun onCategoryClickListener (category: Category)
}