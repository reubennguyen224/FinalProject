package com.training.finalproject

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.training.finalproject.model.Option

class DiffUtilCallbackOption(private val oldList: Option, private val newList: Option) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].nameOption == newList[newItemPosition].nameOption
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].status == newList[newItemPosition].status
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldOption = oldList[oldItemPosition]
        val newOption = newList[newItemPosition]
        val diff = Bundle()
        if (oldOption.status != newOption.status) {
            diff.putBoolean("statusLogin", newOption.status)
        }
        if (diff.size() == 0) return null
        return diff
//        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}