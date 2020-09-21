package com.example.mynext.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynext.ItemClickListener
import com.example.mynext.R
import com.example.mynext.model.Item
import kotlinx.android.synthetic.main.item_card.view.*

class ItemAdapter(
    private val items: List<Item>,
    private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(val card: View) : RecyclerView.ViewHolder(card)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClickListener()
        }

        val item = items[position]
        holder.card.itemcard_title_tv.text = item.title
        holder.card.itemcard_description_tv.text = item.description
        holder.card.itemcard_recommendedby_tv.text = item.recommender
        holder.card.itemcard_image_iv.setImageBitmap(item.image)
    }

    override fun getItemCount(): Int {
        return items.size
    }


}