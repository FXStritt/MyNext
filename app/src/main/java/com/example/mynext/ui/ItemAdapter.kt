package com.example.mynext.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynext.R
import com.example.mynext.fragments.ItemClickListener
import com.example.mynext.model.Item
import com.example.mynext.util.ImageHelper
import kotlinx.android.synthetic.main.item_card.view.*
import java.text.DateFormat
import java.util.*

class ItemAdapter(private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private var items: MutableList<Item> = mutableListOf()

    class ItemViewHolder(val card: View) : RecyclerView.ViewHolder(card)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClickListener(items[position])
        }

        val item = items[position]
        with(holder.card) {
            itemcard_title_tv.text = item.itemTitle
            itemcard_description_tv.text = item.description
            itemcard_recommendedby_tv.text = context.getString(R.string.recommended_by,item.recommender) //TODO should be size 0 if no recommender

            val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
            val formattedDate = dateFormat.format(item.dateCreated)
            itemcard_dateadded_tv.text = context.getString(R.string.dateadded, formattedDate)

            val bitmap = ImageHelper.retrieveBitmapFromFileSystem(context,item.imageName)
            itemcard_image_iv.setImageBitmap(bitmap)

            grayOutFieldsIfItemDone(this, item.done)
        }
    }

    private fun grayOutFieldsIfItemDone(view: View, itemDone : Boolean) {
        with(view) {
            itemcard_title_tv.isEnabled = !itemDone
            itemcard_description_tv.isEnabled = !itemDone
            itemcard_recommendedby_tv.isEnabled = !itemDone
            itemcard_dateadded_tv.isEnabled = !itemDone
            if (itemDone) {
                itemcard_image_iv.setColorFilter(Color.argb(200, 200, 200, 200))
            } else {
                itemcard_image_iv.clearColorFilter()
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(newitems: List<Item>) {
        items = newitems.toMutableList()
        notifyDataSetChanged()
    }



}