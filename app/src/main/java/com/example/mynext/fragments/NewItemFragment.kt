package com.example.mynext.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mynext.R
import com.example.mynext.model.Category
import com.example.mynext.model.CategoryViewModel
import com.example.mynext.model.Item
import com.example.mynext.util.DummyDataProvider
import com.example.mynext.util.ImageRetriever
import kotlinx.android.synthetic.main.fragment_new_item.*
import java.util.*

class NewItemFragment : Fragment() {

    private val selectedCategory: CategoryViewModel by activityViewModels()
    private var chosenImage: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedCategory.selected.observe(viewLifecycleOwner, { category ->
            createitem_maintitle_tv.text = getString(R.string.new_item_title, category.itemsName)
        })

        createitem_chooseimage_iv.setOnClickListener {
            startActivityForResult(ImageRetriever.getImageIntent(), ImageRetriever.CHOOSE_IMAGE_REQUEST_CODE)
        }

        createitem_cancel_btn.setOnClickListener {
            //TODO Modify navigation to clear fragment stack
            findNavController().navigate(R.id.action_NewItemsFragment_to_ItemsFragment)
        }

        createitem_save_btn.setOnClickListener {
            if (allFieldsValid()) { //TODO Add fields validation in allFieldsValid() method
                val newItem = createItemFromFields()

                Log.d("MYTAG", newItem.toString())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ImageRetriever.CHOOSE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK
        ) {
            val uri = data?.data ?: return
            val bitmap = ImageRetriever.getBitmapFromUri(uri, requireActivity())

            bitmap.let {
                createitem_image_iv.setImageBitmap(bitmap)
                chosenImage = bitmap
            }
        }
    }

    private fun allFieldsValid(): Boolean = true

    private fun createItemFromFields() : Item {
        val title = createitem_title_et.text.toString().trim()
        val description = createitem_description_et.text.toString().trim()
        val recommender = createitem_recommender_et.text.toString().trim()
        val category = selectedCategory.selected.value ?: Category("Books","Book","Read") //Dummy category in case of null
        val finalImage = chosenImage ?: DummyDataProvider(context).getDummyBitmap(category) //Dummy bitmap in case of no images chosen

        //TODO compress image upon item creation

        return Item(title, description, recommender, finalImage, category.title, Date())
    }
}