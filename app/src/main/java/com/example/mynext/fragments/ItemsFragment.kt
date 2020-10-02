package com.example.mynext.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynext.R
import com.example.mynext.model.CategoryViewModel
import com.example.mynext.model.Item
import com.example.mynext.model.ItemsViewModel
import com.example.mynext.ui.ItemAdapter
import kotlinx.android.synthetic.main.fragment_items.*


class ItemsFragment : Fragment(), ItemClickListener {

    private val selectedCategory: CategoryViewModel by activityViewModels()
    private lateinit var itemsViewModel: ItemsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        itemsViewModel = ViewModelProvider(this).get(ItemsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        items_newitem_fab.setOnClickListener { findNavController().navigate(R.id.action_ItemsFragment_to_NewItemsFragment) }

        selectedCategory.selected.observe(viewLifecycleOwner, { category ->

            val itemAdapter = ItemAdapter(this)

            itemsViewModel.allItems.observe(viewLifecycleOwner) {
                itemAdapter.setItems(it.filter { item: Item -> item.categoryId == category.title })
            }

            items_recyclerview.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(activity)
                adapter = itemAdapter
            }

            items_categorytitle_tv.text = category.title
            items_newitem_fab.text = getString(R.string.new_item_title, category.itemsName)
        })
    }

    override fun onItemClickListener() {
        Toast.makeText(context, "Implement action",Toast.LENGTH_SHORT).show()
    }
}

interface ItemClickListener {
    fun onItemClickListener ()
}
