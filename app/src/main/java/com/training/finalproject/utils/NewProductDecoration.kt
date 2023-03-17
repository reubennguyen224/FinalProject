package com.training.finalproject.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NewProductDecoration(private val offset: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val gridLayoutManager = parent.layoutManager as GridLayoutManager
        val spanCount = gridLayoutManager.spanCount
        val params = view.layoutParams as GridLayoutManager.LayoutParams

        val spanIndex = params.spanIndex
        val spanSize = params.spanSize

        if (spanSize < 2) {
            if (spanIndex == 0) {
                outRect.left = offset * 3

            } else {
                outRect.left = offset / 2
            }

            if (spanIndex + spanSize == spanCount) {
                outRect.right = offset /2
            } else {
                outRect.right = offset / 2
            }
            outRect.top = offset / 2
            outRect.bottom = offset
        }

    }
}

