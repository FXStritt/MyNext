package com.example.mynext

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mynext.model.Category
import com.example.mynext.model.CategoryViewModel
import com.example.mynext.ui.CategoryAdapter
import com.example.mynext.util.DummyDataProvider
import kotlinx.android.synthetic.main.fragment_first.*


class CategoriesFragment : Fragment(), CategoryClickListener {

    private val selectedCategory: CategoryViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryAdapter = CategoryAdapter(DummyDataProvider(context).getDummyCategories(),this)

        categories_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity,2, GridLayoutManager.VERTICAL, false)
            adapter = categoryAdapter
        }
    }

    override fun onCategoryClickListener(category: Category) {
        selectedCategory.select(category)

        findNavController().navigate(R.id.action_CategoriesFragment_to_ItemsFragment)
    }
}

interface CategoryClickListener {
    fun onCategoryClickListener (category: Category)
}