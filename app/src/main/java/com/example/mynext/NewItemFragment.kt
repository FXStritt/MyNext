package com.example.mynext

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mynext.model.Category
import com.example.mynext.model.CategoryViewModel
import com.example.mynext.model.Item
import com.example.mynext.util.DummyDataProvider
import kotlinx.android.synthetic.main.fragment_new_item.*
import java.io.IOException
import java.util.*

class NewItemFragment : Fragment() {

    companion object {
        private const val CHOOSE_IMAGE_REQUEST = 101
    }

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
            createitem_maintitle_tv.text = getString(R.string.new_item_title, category.title)
        })

        createitem_chooseimage_iv.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            //Android will find the best app to fulfill this intent
            val chooser = Intent.createChooser(intent, "Choose image for habit")
            startActivityForResult(chooser, CHOOSE_IMAGE_REQUEST)
        }

        createitem_cancel_btn.setOnClickListener {
            findNavController().navigate(R.id.action_NewItemsFragment_to_ItemsFragment)
        }

        createitem_save_btn.setOnClickListener {
            if (allFieldsValid()) { //TODO Add fields validation in allFieldsValid() method

                val title = createitem_title_et.text.toString().trim()
                val description = createitem_description_et.text.toString().trim()
                val recommender = createitem_recommender_et.text.toString().trim()
                val category = selectedCategory.selected.value ?: Category("Books") //Dummy category in case of null
                val finalImage = chosenImage ?: DummyDataProvider(context).getDummyBitmap(category) //Dummy bitmap in case of no images chosen

                val newItem = Item(
                    title,
                    description,
                    recommender,
                    finalImage,
                    category,
                    Date()
                )

                Log.d("MYTAG",newItem.toString())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CHOOSE_IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null && data.data != null
        ) {
            val bitmap = tryReadBitmap(data.data ?: return)

            bitmap.let {
                createitem_image_iv.setImageBitmap(bitmap)
            }
        }
    }

    private fun tryReadBitmap(data: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT < 28) {
                //deprecated in API 29
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data)
            } else {
                val source = ImageDecoder.createSource(requireActivity().contentResolver, data)
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: IOException) {
            null
        }
    }

    private fun allFieldsValid(): Boolean = true
}