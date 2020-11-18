package com.example.mynext.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.navigation.NavController
import com.example.mynext.R
import com.example.mynext.fragments.ItemDialogCallback
import com.example.mynext.model.Item
import com.example.mynext.model.SelectedItemViewModel
import com.example.mynext.util.ImageHelper
import kotlinx.android.synthetic.main.dialog_item_details.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ItemDialogBuilder(val context: Context, private val itemDialogCallback: ItemDialogCallback,
                        private val navController: NavController, private val selectedItem: SelectedItemViewModel) {

    fun getItemDialog(item: Item, itemsName: String?): Dialog {

        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = View.inflate(context, R.layout.dialog_item_details, null)

        setItemInformation(dialogView, item)

        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()

        //Button's actions are set after dialog creation as we need its reference to dismiss it
        setButtonsActions(dialogView, item, itemsName ?: "", dialog)

        return dialog
    }

    private fun setItemInformation(dialogView: View, item: Item) {
        with(dialogView) {
            itemdialog_title_tv.text = item.itemTitle
            itemdialog_description_tv.text = item.description
            itemdialog_recommender_tv.text = context.getString(
                R.string.recommended_by,
                item.recommender
            )

            val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
            val formattedDate: String = item.dateCreated.format(formatter)
            itemdialog_date_tv.text = context.getString(R.string.dateadded, formattedDate)

            val bitmap = ImageHelper.retrieveBitmapFromFileSystem(context, item.imageName)
            itemdialog_image_iv.setImageBitmap(bitmap)

            if (item.done) {
                itemdialog_markasdone_btn.setBackgroundColor(
                    resources.getColor(
                        R.color.colorDone,
                        context.theme
                    )
                )
                itemdialog_markasdone_btn.setIconResource(R.drawable.ic_baseline_check_box_24)
                item_dialog_layout.setBackgroundResource(R.drawable.background_gray)
            } else {
                itemdialog_markasdone_btn.setBackgroundColor(
                    resources.getColor(
                        R.color.colorAccent,
                        context.theme
                    )
                )
                itemdialog_markasdone_btn.setIconResource(R.drawable.ic_baseline_check_box_outline_blank_24)
                item_dialog_layout.setBackgroundResource(R.drawable.background_accent)
            }
        }
    }

    private fun setButtonsActions(dialogView: View, item: Item, itemsName: String, dialog: Dialog) {

        with(dialogView.itemdialog_findonline_btn) {
            setOnClickListener {
                //Example of generated url: https://www.google.com/search?q=Book+To+kill+a+Mockingbird
                val url = "https://www.google.com/search?q=$itemsName+${item.itemTitle}"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(browserIntent)
            }
        }

        with(dialogView.itemdialog_markasdone_btn) {
            setOnClickListener {
                if (item.done) {
                    item.done = false
                    setBackgroundColor(resources.getColor(R.color.colorAccent, context.theme))
                    setIconResource(R.drawable.ic_baseline_check_box_outline_blank_24)
                    dialogView.item_dialog_layout.setBackgroundResource(R.drawable.background_accent)
                } else {
                    item.done = true
                    item.dateDone = LocalDateTime.now()
                    setBackgroundColor(resources.getColor(R.color.colorDone, context.theme))
                    setIconResource(R.drawable.ic_baseline_check_box_24)
                    dialogView.item_dialog_layout.setBackgroundResource(R.drawable.background_gray)
                }
                itemDialogCallback.updateItem(item)
            }
        }

        with(dialogView.itemdialog_edit_btn) {
            setOnClickListener {
                selectedItem.select(item)
                navController.navigate(R.id.action_ItemsFragment_to_NewItemsFragment)
                dialog.dismiss()
            }
        }
    }
}