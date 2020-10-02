package com.example.mynext.fragments

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mynext.R
import com.example.mynext.model.CategoriesViewModel
import com.example.mynext.model.Category
import com.example.mynext.util.DummyDataProvider
import com.example.mynext.util.ImageRetriever
import kotlinx.android.synthetic.main.fragment_new_category.*


class NewCategoryFragment : Fragment() {

    private var chosenImage: Bitmap? = null

    private lateinit var categoryViewModel: CategoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryViewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)

        createcateg_chooseimage_iv.setOnClickListener {
            startActivityForResult(ImageRetriever.getImageIntent(), ImageRetriever.CHOOSE_IMAGE_REQUEST_CODE)
        }

        createcateg_cancel_btn.setOnClickListener {
            findNavController().navigateUp()
        }

        createcateg_save_btn.setOnClickListener {
            if (allFieldsValid()) { //TODO Add fields validation in allFieldsValid() method
                val newCategory = createCategoryFromFields()

                categoryViewModel.insert(newCategory)

                Log.d("MYTAG", newCategory.toString())

                view.let {//hide keyboard after category inserted
                    val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(it.windowToken, 0)
                }
                findNavController().navigateUp()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ImageRetriever.CHOOSE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK
        ) {
            val uri = data?.data ?: return
            val bitmap = ImageRetriever.getBitmapFromUri(uri, requireActivity())

            bitmap.let {
                createcateg_image_iv.setImageBitmap(bitmap)
                chosenImage = bitmap
            }
        }
    }

    private fun allFieldsValid(): Boolean = true

    private fun createCategoryFromFields() : Category {

        val name = createcateg_categname_et.text.toString().trim()
        val itemsName = createcateg_itemsname_et.text.toString().trim()
        val verb = createcateg_verb_et.text.toString().trim()
        val finalImage = chosenImage ?: DummyDataProvider(context).getDummyBitmap("Books") //Dummy bitmap in case of no images chosen

        return Category(name, itemsName, verb, finalImage)
    }
}