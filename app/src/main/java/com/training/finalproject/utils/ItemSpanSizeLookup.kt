package com.training.finalproject.utils

import androidx.recyclerview.widget.GridLayoutManager
import com.training.finalproject.home_activity.dashboard.home.adapter.BANNER
import com.training.finalproject.home_activity.dashboard.home.adapter.HomeFragmentAdapter
import com.training.finalproject.home_activity.dashboard.home.adapter.NEWPRODUCT
import com.training.finalproject.home_activity.dashboard.home.adapter.TITLE

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