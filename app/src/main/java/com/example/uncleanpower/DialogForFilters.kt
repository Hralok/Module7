package com.example.uncleanpower

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DialogForFilters : DialogFragment() {

    private val filterNames = arrayOf("Цветокоррекция", "Серый мир",  "Негатив", "Отражатель", "Размытие")
    private var checkedItems = booleanArrayOf(false, false, false, false, false)

    fun setStartValues(needColCor:Boolean, needGW:Boolean, needInv:Boolean, needPerRef:Boolean, needBlur:Boolean){
        checkedItems = booleanArrayOf(needColCor, needGW,  needInv, needPerRef, needBlur)
    }

    fun returnValues():BooleanArray{
        return checkedItems
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Выберите фильтры")
                .setMultiChoiceItems(filterNames, checkedItems) {
                        dialog, which, isChecked ->
                    checkedItems[which] = isChecked
                }
                .setPositiveButton("Готово"
                ) {
                        dialog, id -> dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}