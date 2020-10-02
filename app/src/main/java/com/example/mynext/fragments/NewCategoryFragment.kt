package com.example.mynext.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mynext.R
import com.example.mynext.model.CategoriesViewModel
import com.example.mynext.model.Category
import com.example.mynext.util.ContextHelper
import com.example.mynext.util.ImageRetriever
import kotlinx.android.synthetic.main.fragment_new_category.*


class NewCategoryFragment : Fragment() {

    private var chosenImage: Bitmap? = null

    private lateinit var categoryViewModel: CategoriesViewModel

    private lateinit var allCategories: List<Category>

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
        categoryViewModel.allCategories.observe(viewLifecycleOwner) {
            allCategories = it
        }

        createcateg_chooseimage_iv.setOnClickListener {
            startActivityForResult(ImageRetriever.getImageIntent(), ImageRetriever.CHOOSE_IMAGE_REQUEST_CODE)
        }

        createcateg_cancel_btn.setOnClickListener {
            findNavController().navigateUp()
        }

        createcateg_save_btn.setOnClickListener {
            if (allFieldsValid() && categoryDoesNotExist()) { //TODO verify that category name does not exist yet.
                createcateg_errortext_tv.text = "" //Remove any error messages

                val newCategory = createCategoryFromFields()

                categoryViewModel.insert(newCategory)

                ContextHelper.hideSoftKeyboard(view,context)

                findNavController().navigateUp()

            } else {
                showErrorMessage()
            }
        }
    }

    private fun categoryDoesNotExist(): Boolean {

        for (category in allCategories) {
            if (category.title == createcateg_categname_et.text.toString().trim()) {
                createcateg_errortext_tv.text = getString(R.string.newcateg_error_categexists)
                return false
            }
        }

        return true
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

    private fun allFieldsValid(): Boolean {
        return if (createcateg_categname_et.text.toString().trim().isNotEmpty()
            && createcateg_itemsname_et.text.toString().trim().isNotEmpty()
            && createcateg_verb_et.text.toString().trim().isNotEmpty()) {
            true
        } else {
            createcateg_errortext_tv.text = getString(R.string.newcateg_error_invalidfields, "category")
            false
        }
    }

    private fun createCategoryFromFields() : Category {

        val name = createcateg_categname_et.text.toString().trim()
        val itemsName = createcateg_itemsname_et.text.toString().trim()
        val verb = createcateg_verb_et.text.toString().trim()
//        val finalImage = chosenImage ?: DummyDataProvider(context).getDummyBitmap("Books") //Dummy bitmap in case of no images chosen

        return Category(name, itemsName, verb)
    }

    private fun showErrorMessage() {

        with(createcateg_errortext_tv) {
            if (this.text.trim().isNotEmpty()) {
                this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.blink_error))
            }
        }
    }
}