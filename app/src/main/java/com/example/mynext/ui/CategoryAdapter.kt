package com.example.mynext.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynext.CategoryClickListener
import com.example.mynext.R
import com.example.mynext.model.Category
import kotlinx.android.synthetic.main.category_card.view.*

class CategoryAdapter(
    private val categories: List<Category>,
    private val categoryClickListener: CategoryClickListener,
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(val card: View) : RecyclerView.ViewHolder(card)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_card, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            categoryClickListener.onCategoryClickListener(categories[position])
        }

        val category = categories[position]
        holder.card.categorycard_title_tv.text = category.title
    }

    override fun getItemCount(): Int {
        return categories.size //dummy value until list of categories implemented
    }


}