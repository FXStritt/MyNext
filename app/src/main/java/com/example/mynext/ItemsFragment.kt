package com.example.mynext

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynext.model.CategoryViewModel
import com.example.mynext.ui.ItemAdapter
import com.example.mynext.util.DummyDataProvider
import kotlinx.android.synthetic.main.fragment_second.*


class ItemsFragment : Fragment(), ItemClickListener {

    private val selectedCategory: CategoryViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedCategory.selected.observe(viewLifecycleOwner, { category ->

            val itemAdapter = ItemAdapter(DummyDataProvider(context).getDummyItemFromCategory(category),this)

            items_recyclerview.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(activity)
                adapter = itemAdapter
            }

        })
    }

    override fun onItemClickListener() {
        Toast.makeText(context, "Implement action",Toast.LENGTH_SHORT).show()
    }
}

interface ItemClickListener {
    fun onItemClickListener ()
}
