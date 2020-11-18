package com.example.mynext.fragments

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynext.R
import com.example.mynext.model.Item
import com.example.mynext.model.ItemsViewModel
import com.example.mynext.model.SelectedCategoryViewModel
import com.example.mynext.model.SelectedItemViewModel
import com.example.mynext.ui.ItemAdapter
import com.example.mynext.ui.ItemDialogBuilder
import com.example.mynext.ui.SwipeToDeleteCallback
import com.example.mynext.util.ImageHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_items.*


class ItemsFragment : Fragment(), ItemClickListener, ItemSwipeListener, ItemDialogCallback {

    //TODO selectedCategory could be a new ViewModel with type CategoryWithItems, avoiding items loading and filtering in this fragment
    private val selectedCategory: SelectedCategoryViewModel by activityViewModels()
    private val selectedItem : SelectedItemViewModel by activityViewModels()
    private lateinit var itemsViewModel: ItemsViewModel
    private var tempBitmapIfItemRestore : Bitmap? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        itemsViewModel = ViewModelProvider(this).get(ItemsViewModel::class.java)
        selectedItem.select(null) //ensures no item "selected" when we get / return to this fragment

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

            val itemAdapter = ItemAdapter(this, this)

            itemsViewModel.allItems.observe(viewLifecycleOwner) {
                itemAdapter.setItems(it.filter { item: Item -> item.categoryId == category.title })
            }

            items_recyclerview.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(activity)
                adapter = itemAdapter


                val itemTouchHelper = ItemTouchHelper(
                    SwipeToDeleteCallback(
                        itemAdapter,
                        ColorDrawable(ContextCompat.getColor(context, R.color.colorError)),
                        ContextCompat.getDrawable(context,R.drawable.ic_baseline_delete_24)))

                itemTouchHelper.attachToRecyclerView(this)
            }

            items_categorytitle_tv.text = category.title
            items_newitem_fab.text = getString(R.string.new_item_title, category.itemsName)
        })
    }

    override fun onItemClickListener(item: Item) {
        val itemDialogBuilder = ItemDialogBuilder(requireContext(), this, findNavController(), selectedItem)
        val itemDialog = itemDialogBuilder.getItemDialog(item = item, itemsName =  selectedCategory.selected.value?.itemsName)
        itemDialog.show()
    }
    override fun updateItem(item: Item) {
        itemsViewModel.updateItem(item)
    }

    override fun deleteItem(item: Item) {
        itemsViewModel.deleteItem(item, selectedCategory.selected.value?.imageName)
    }

    override fun onItemSwipeToLeft(item: Item) { //not calling deleteItem() as it belongs to another interface
        tempBitmapIfItemRestore = ImageHelper.retrieveBitmapFromFileSystem(requireContext(),item.imageName)
        itemsViewModel.deleteItem(item, selectedCategory.selected.value?.imageName)
        val undoSnackbar = Snackbar.make(requireView(), "Deleted ${item.itemTitle}", Snackbar.LENGTH_LONG)
        undoSnackbar.setAction("undo") {
            itemsViewModel.insert(item, tempBitmapIfItemRestore!!)
        }
        undoSnackbar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        undoSnackbar.show()
    }

    override fun onItemSwipeToRight() {
        findNavController().navigateUp()
    }
}

interface ItemClickListener {
    fun onItemClickListener(item: Item)
}

interface ItemSwipeListener {
    fun onItemSwipeToLeft(item: Item)
    fun onItemSwipeToRight()
}

interface ItemDialogCallback {
    fun updateItem(item: Item)
    fun deleteItem(item: Item)
}


