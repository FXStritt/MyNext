package com.example.mynext.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.example.mynext.util.ImageHelper
import kotlinx.android.synthetic.main.dialog_item_details.view.*
import kotlinx.android.synthetic.main.fragment_items.*
import java.text.DateFormat
import java.util.*


class ItemsFragment : Fragment(), ItemClickListener {

    //TODO selectedCategory could be a new ViewModel with type CategoryWithItems, avoiding items loading and filtering in this fragment
    private val selectedCategory: SelectedCategoryViewModel by activityViewModels()
    private lateinit var itemsViewModel: ItemsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        itemsViewModel = ViewModelProvider(this).get(ItemsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_items, container, false)
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

    override fun onItemClickListener(item: Item) { //on item selection, create alert dialog showing item details & possible actions
        val dialogBuilder = AlertDialog.Builder(activity)
        val dialogView = View.inflate(context, R.layout.dialog_item_details, null)

        setItemInformation(dialogView, item)
        setButtonsActions(dialogView, item)

        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun setItemInformation(dialogView: View, item: Item) {
        with(dialogView) {
            itemdialog_title_tv.text = item.itemTitle
            itemdialog_description_tv.text = item.description
            itemdialog_recommender_tv.text = context.getString(
                R.string.recommended_by,
                item.recommender
            )

            val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
            val formattedDate = dateFormat.format(item.dateCreated)
            itemdialog_date_tv.text = context.getString(R.string.dateadded, formattedDate)

            val bitmap = ImageHelper.retrieveBitmapFromFileSystem(context, item.imageName)
            itemdialog_image_iv.setImageBitmap(bitmap)
        }
    }

    private fun setButtonsActions(dialogView: View, item: Item) {
        with(dialogView) {
            itemdialog_findonline_btn.setOnClickListener {
                //Example of generated url: https://www.google.com/search?q=Book+To+kill+a+Mockingbird
                val url = "https://www.google.com/search?q=${selectedCategory.selected.value?.itemsName}+${item.itemTitle}"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }

            itemdialog_markasdone_btn.setOnClickListener {
                Log.d("MYTAG", "mark as done")
            }

            itemdialog_edit_btn.setOnClickListener {
                Log.d("MYTAG", "edit item")
            }

            itemdialog_delete_btn.setOnClickListener {
                Log.d("MYTAG", "delete item")
            }

        }
    }
}

interface ItemClickListener {
    fun onItemClickListener(item: Item)
}
