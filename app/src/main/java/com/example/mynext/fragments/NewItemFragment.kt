package com.example.mynext.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mynext.R
import com.example.mynext.model.Item
import com.example.mynext.model.ItemsViewModel
import com.example.mynext.model.SelectedCategoryViewModel
import com.example.mynext.model.SelectedItemViewModel
import com.example.mynext.util.ContextHelper
import com.example.mynext.util.ImageHelper
import kotlinx.android.synthetic.main.fragment_new_item.*
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class NewItemFragment : Fragment() {

    private val selectedCategory: SelectedCategoryViewModel by activityViewModels()
    private val itemToUpdate : SelectedItemViewModel by activityViewModels()
    private lateinit var chosenImage: Bitmap
    private var imageIsDummy = true
    private lateinit var itemsViewModel: ItemsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemsViewModel = ViewModelProvider(this).get(ItemsViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_new_item, container, false)
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

        selectedCategory.selected.observe(viewLifecycleOwner, { category ->

            if (itemToUpdate.selected.value != null) { //if an item is selected, we initialize the layout with its content
                val item = itemToUpdate.selected.value
                createitem_maintitle_tv.text = getString(R.string.update_item_title, category.itemsName)
                createitem_title_et.setText(item?.itemTitle, TextView.BufferType.EDITABLE)
                createitem_description_et.setText(item?.description, TextView.BufferType.EDITABLE)
                createitem_recommender_et.setText(item?.recommender, TextView.BufferType.EDITABLE)
                chosenImage = ImageHelper.retrieveBitmapFromFileSystem(
                    requireContext(),
                    item?.imageName ?: "null"
                )
            } else { //otherwise we initialize the layout with default content
                createitem_maintitle_tv.text =
                    getString(R.string.new_item_title, category.itemsName)
                chosenImage = ImageHelper.retrieveBitmapFromFileSystem(
                    requireContext(),
                    category.imageName
                )
            }

            createitem_image_iv.setImageBitmap(chosenImage)
        })

        createitem_chooseimage_iv.setOnClickListener {
            startActivityForResult(
                ImageHelper.getImageIntent(requireContext()),
                ImageHelper.CHOOSE_IMAGE_REQUEST_CODE
            )
        }

        createitem_cancel_btn.setOnClickListener {
            ContextHelper.hideSoftKeyboard(view,context)
            findNavController().navigateUp()
        }

        createitem_save_btn.setOnClickListener {

            if (allFieldsValid() && categorySelected()) {
                createitem_errortext_tv.text = "" //Clear any potential error messages

                //TODO Manage image deletion upon update
                if (itemToUpdate.selected.value?.itemId != null) {
                    updateItemFromFields()
                } else {
                    val newItem = createItemFromFields()
                    itemsViewModel.insert(newItem, chosenImage)
                }


                ImageHelper.deleteTempImageFileIfExist(requireContext()) //temp image returned by camera no longer needed

                ContextHelper.hideSoftKeyboard(view, context)
                findNavController().navigateUp()
            } else {
                showErrorMessage()
            }
        }
    }

    private fun allFieldsValid():Boolean {
        return (createitem_title_et.text.toString().trim().isNotEmpty()
                && createitem_description_et.text.toString().trim().isNotEmpty())
    }

    private fun categorySelected(): Boolean {
        return selectedCategory.selected.value?.title != null
    }

    private fun showErrorMessage() {

        with(createitem_errortext_tv) {
            if (this.text.trim().isEmpty()) {
                this.text = getString(
                    R.string.newitem_error_message,
                    selectedCategory.selected.value?.itemsName?.toLowerCase(Locale.ROOT)
                )
            } else {
                this.startAnimation(
                    AnimationUtils.loadAnimation(
                        context,
                        R.anim.blink_error
                    )
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ImageHelper.CHOOSE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK
        ) {
            val bitmap = if (data?.dataString != null) { //Image was taken in storage
                val uri = data.data ?: return
                ImageHelper.getBitmapFromUri(uri, requireActivity())
            } else { //image was taken using camera
                ImageHelper.getImageTakenByCamera(requireActivity())
            }

            bitmap?.let {
                createitem_image_iv.setImageBitmap(it)
                chosenImage = bitmap
                imageIsDummy = false
            }
        }
    }

    private fun createItemFromFields() : Item {
        val title = createitem_title_et.text.toString().trim()
        val description = createitem_description_et.text.toString().trim()
        val recommender = createitem_recommender_et.text.toString().trim()
        val category = selectedCategory.selected.value?.title
        val date = LocalDateTime.now()

        var imageName = title + date.toEpochSecond(ZoneOffset.UTC)
        if (imageIsDummy && selectedCategory.selected.value?.imageName != null) {
            imageName = selectedCategory.selected.value?.imageName.toString()
        }

        return Item(title, description, recommender, imageName, category ?: "NA", LocalDateTime.now())
    }

    private fun updateItemFromFields() {
        //TODO Manage image change, creation & deletion
        val updatedItem = createItemFromFields()
        val itemId = itemToUpdate.selected.value!!.itemId
        val dateCreated = itemToUpdate.selected.value?.dateCreated ?: LocalDateTime.now()
        updatedItem.itemId = itemId
        updatedItem.dateCreated = dateCreated
        itemsViewModel.updateItem(updatedItem)
    }
}