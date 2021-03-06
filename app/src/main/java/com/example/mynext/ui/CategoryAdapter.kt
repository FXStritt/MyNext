package com.example.mynext.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynext.R
import com.example.mynext.fragments.CategoryClickListener
import com.example.mynext.model.CategoriesWithItems
import com.example.mynext.model.Category
import kotlinx.android.synthetic.main.category_card.view.*
import java.time.LocalDateTime

class CategoryAdapter(
    private val categoryClickListener: CategoryClickListener,
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var categoriesWithItems: MutableList<CategoriesWithItems> = mutableListOf()

    companion object {
        private const val TYPE_CATEGORY = 1
        private const val TYPE_ADD = 2
        const val ADD_CATEGORY = "ADD_CATEGORY"
    }

    init { //adds a category used by getItemViewType() to have a layout that shows a "+ category" item
        categoriesWithItems.add(getNewAddCategory())
    }

    class CategoryViewHolder(val card: View) : RecyclerView.ViewHolder(card)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return when (viewType) {
            TYPE_CATEGORY -> CategoryViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.category_card, parent, false)
            )
            else -> CategoryViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.category_card_add, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            categoryClickListener.onCategoryClickListener(categoriesWithItems[position])
        }

        if (getItemViewType(position) == TYPE_CATEGORY) {

            val category = categoriesWithItems[position].category
            val text = "to ${category.verb}"
            val itemsToDoCount = categoriesWithItems[position]
                .items
                .stream()
                .filter {!it.done}
                .count()
            val itemsDoneThisMonth = categoriesWithItems[position]
                .items
                .stream()
                .filter { it.done
                        && it.dateDone.month == LocalDateTime.now().month
                        && it.dateDone.year == LocalDateTime.now().year }
                .count()

            with(holder.card) {
                categorycard_title_tv.text = category.title
                categorycard_todo_tv.text = text
                categorycard_number_tv.text = itemsToDoCount.toString()
                categorycard_numberdone_tv.text  = context.getString(R.string.categorycard_numdone,itemsDoneThisMonth)
            }

        }
    }

    override fun getItemCount(): Int {
        return categoriesWithItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (categoriesWithItems[position].category.title == ADD_CATEGORY) {
            TYPE_ADD
        } else {
            TYPE_CATEGORY
        }
    }

    fun setCategories(newCategoriesWithItems: List<CategoriesWithItems>) {
        categoriesWithItems = newCategoriesWithItems.toMutableList()
        categoriesWithItems.add(getNewAddCategory())

        notifyDataSetChanged()
    }

    private fun getNewAddCategory(): CategoriesWithItems {
        return CategoriesWithItems(Category(ADD_CATEGORY, "", ""), emptyList())
    }
}