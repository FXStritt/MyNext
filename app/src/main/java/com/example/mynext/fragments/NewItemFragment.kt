package com.example.mynext.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mynext.R
import com.example.mynext.model.Item
import com.example.mynext.model.ItemsViewModel
import com.example.mynext.model.SelectedCategoryViewModel
import com.example.mynext.util.ContextHelper
import com.example.mynext.util.ImageHelper
import kotlinx.android.synthetic.main.fragment_new_item.*
import java.util.*

class NewItemFragment : Fragment() {

    private val selectedCategory: SelectedCategoryViewModel by activityViewModels()
    private lateinit var chosenImage: Bitmap
    private lateinit var itemsViewModel: ItemsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        itemsViewModel = ViewModelProvider(this).get(ItemsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_new_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set main title (New itemXYZ) and dummy image of category
        selectedCategory.selected.observe(viewLifecycleOwner, { category ->
            createitem_maintitle_tv.text = getString(R.string.new_item_title, category.itemsName)
            chosenImage = ImageHelper.retrieveBitmapFromFileSystem(requireContext(), category.imageName)
            createitem_image_iv.setImageBitmap(chosenImage)
        })

        createitem_chooseimage_iv.setOnClickListener {
            startActivityForResult(ImageHelper.getImageIntent(), ImageHelper.CHOOSE_IMAGE_REQUEST_CODE)
        }

        createitem_cancel_btn.setOnClickListener {
            findNavController().navigateUp()
        }

        createitem_save_btn.setOnClickListener {

            if (allFieldsValid() && categorySelected()) {
                createitem_errortext_tv.text = "" //Clear any potential error messages

                val newItem = createItemFromFields()

                itemsViewModel.insert(newItem, chosenImage)

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
                this.text = getString(R.string.newitem_error_message,
                    selectedCategory.selected.value?.itemsName?.toLowerCase(Locale.ROOT)
                )
            } else {
                this.startAnimation(AnimationUtils.loadAnimation(context,
                    R.anim.blink_error))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ImageHelper.CHOOSE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK
        ) {
            val uri = data?.data ?: return
            val bitmap = ImageHelper.getBitmapFromUri(uri, requireActivity())

            bitmap.let {
                createitem_image_iv.setImageBitmap(bitmap)
                if (bitmap != null) {
                    chosenImage = bitmap
                }
            }
        }
    }

    private fun createItemFromFields() : Item {
        val title = createitem_title_et.text.toString().trim()
        val description = createitem_description_et.text.toString().trim()
        val recommender = createitem_recommender_et.text.toString().trim()
        val category = selectedCategory.selected.value?.title
        val date = Date()

        //TODO verify if image is different then dummy. If not, title should not be different than dummy image to avoid double saving

        return Item(title, description, recommender,title + date.time , category ?: "NA", Date())
    }
}