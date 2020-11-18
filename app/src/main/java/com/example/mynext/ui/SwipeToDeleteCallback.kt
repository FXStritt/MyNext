package com.example.mynext.ui

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDeleteCallback(
    private var mAdapter: ItemAdapter,
    private val background: ColorDrawable,
    private val icon: Drawable?
)
    : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false //function not used
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        mAdapter.deleteItem(position) //TODO remove this once item is deleted from DB
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                             dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20 //used to push background behind the edge of the parent view to appear under rounded corners

        val iconIntrinsicHeight = icon?.intrinsicHeight ?: 0
        val iconMargin = (itemView.height - iconIntrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - iconIntrinsicHeight) / 2
        val iconBottom = iconTop + iconIntrinsicHeight

        when {
            dX > 0 -> { //swiping to the right
                //TODO implement navigation up to previous fragment

            }
            dX < 0 -> { //swiping to the left
                background.setBounds(
                    itemView.right + dX.toInt() - backgroundCornerOffset,
                    itemView.top + 10,
                    itemView.right,
                    itemView.bottom - 10
                )

                val iconLeft = itemView.right - iconMargin - iconIntrinsicHeight
                val iconRight = itemView.right - iconMargin
                icon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            }
            else -> { // view is unSwiped
                background.setBounds(0, 0, 0, 0)
            }
        }

        background.draw(c)
        icon?.draw(c)
    }
}