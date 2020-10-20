package com.example.mynext.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynext.R
import com.example.mynext.model.Item
import com.example.mynext.model.ItemsViewModel
import com.example.mynext.model.SelectedCategoryViewModel
import com.example.mynext.ui.ItemAdapter
import com.example.mynext.ui.ItemDialogBuilder
import kotlinx.android.synthetic.main.fragment_items.*


class ItemsFragment : Fragment(), ItemClickListener, ItemDialogCallback {

    //TODO selectedCategory could be a new ViewModel with type CategoryWithItems, avoiding items loading and filtering in this fragment
    private val selectedCategory: SelectedCategoryViewModel by activityViewModels()
    private lateinit var itemsViewModel: ItemsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        itemsViewModel = ViewModelProvider(this).get(ItemsViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_items, container, false)
        val swipeGestureNavigator = SwipeGestureNavigator(findNavController())
        view.setOnTouchListener { v, motionEvent ->
            v.performClick()
            swipeGestureNavigator.navigateUpIfSwipeRight(motionEvent)
            true
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        items_newitem_fab.setOnClickListener { findNavController().navigate(R.id.action_ItemsFragment_to_NewItemsFragment) }

        //once fragment knows which category/items to display, initialize recyclerview
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

    override fun onItemClickListener(item: Item) {
        val itemDialogBuilder = ItemDialogBuilder(requireContext(), this)
        val itemDialog = itemDialogBuilder.getItemDialog(item = item, itemsName =  selectedCategory.selected.value?.itemsName)
        itemDialog.show()
    }
    override fun updateItem(item: Item) {
        itemsViewModel.updateItem(item)
    }

    override fun deleteItem(item: Item) {
        itemsViewModel.deleteItem(item, selectedCategory.selected.value?.imageName)
    }
}

interface ItemClickListener {
    fun onItemClickListener(item: Item)
}

interface ItemDialogCallback {
    fun updateItem(item: Item)
    fun deleteItem(item: Item)
}


