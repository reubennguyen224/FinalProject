package com.training.finalproject.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewLoadMoreScroll(layoutManger: GridLayoutManager) :
    RecyclerView.OnScrollListener() {
    private var isLoading = false
    private var lastVisibleProductPosition = 0
    private var totalItemCount = 0
    private var homeLayoutManager: RecyclerView.LayoutManager
    private var visibleThresholds = 6
    private lateinit var onLoadMoreListener: OnLoadMoreListener

    init {
        homeLayoutManager = layoutManger
        visibleThresholds *= layoutManger.spanCount
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy <= 0) {
            return
        }

        totalItemCount = homeLayoutManager.itemCount

        lastVisibleProductPosition =
            (homeLayoutManager as GridLayoutManager).findLastVisibleItemPosition()

        if (!isLoading && totalItemCount < lastVisibleProductPosition + visibleThresholds) {
            onLoadMoreListener.onLoadMore()
            isLoading = true
        }


    }

    fun setOnLoadMoreListener(listener: OnLoadMoreListener) {
        onLoadMoreListener = listener
    }

    fun setLoaded() {
        isLoading = false
    }

    fun getLoaded() = isLoading

    fun getLastVisibleId(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }
}

interface OnLoadMoreListener {
    fun onLoadMore()
}