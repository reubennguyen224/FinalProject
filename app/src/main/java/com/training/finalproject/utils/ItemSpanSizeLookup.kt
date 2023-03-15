package com.training.finalproject.utils

import androidx.recyclerview.widget.GridLayoutManager
import com.training.finalproject.adapter.BANNER
import com.training.finalproject.adapter.HomeFragmentAdapter
import com.training.finalproject.adapter.NEWPRODUCT
import com.training.finalproject.adapter.TITLE

class ItemSpanSizeLookup(private val adapter: HomeFragmentAdapter) :
    GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        return when (adapter.getItemViewType(position)) {
            BANNER -> {
                2
            }
            TITLE -> {
                2
            }
            NEWPRODUCT -> {
                1
            }
            else -> -1
        }
    }
}