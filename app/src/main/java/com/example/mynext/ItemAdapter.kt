package com.example.mynext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder (card: View) : RecyclerView.ViewHolder(card)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // TODO retrieve item values based on position and modifie UI elements
    }

    override fun getItemCount(): Int {
        return 10 //dummy value until list of categories implemented
    }


}