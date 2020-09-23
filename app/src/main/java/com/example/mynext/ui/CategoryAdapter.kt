package com.example.mynext.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynext.fragments.CategoryClickListener
import com.example.mynext.R
import com.example.mynext.model.Category
import kotlinx.android.synthetic.main.category_card.view.*

class CategoryAdapter(
    private val categories: MutableList<Category>,
    private val categoryClickListener: CategoryClickListener,
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    companion object {
        private const val TYPE_CATEGORY = 1
        private const val TYPE_ADD = 2
        const val ADD_CATEGORY = "ADD_CATEGORY"
    }

    init { //adds a category used by getItemViewType() to have a layout that shows a "+ category" item
        categories.add(Category(ADD_CATEGORY,"",""))
    }


    class CategoryViewHolder(val card: View) : RecyclerView.ViewHolder(card)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return when (viewType) {
            TYPE_CATEGORY -> CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_card, parent, false))
            else -> CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_card_add, parent, false))
        }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            categoryClickListener.onCategoryClickListener(categories[position])
        }

        if (getItemViewType(position) == TYPE_CATEGORY) {
            val category = categories[position]
            holder.card.categorycard_title_tv.text = category.title

            val text = "to ${category.verb}"
            holder.card.categorycard_todo_tv.text = text
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (categories[position].title == ADD_CATEGORY) {
            TYPE_ADD
        } else {
            TYPE_CATEGORY
        }
    }
}