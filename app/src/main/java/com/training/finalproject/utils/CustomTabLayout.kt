package com.training.finalproject.utils

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout

class CustomTabLayout(context: Context): TabLayout(context) {
    constructor(context: Context, attrs: AttributeSet): this(context)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): this(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) { //calculate min width of each tab layout
        val tabLayout = getChildAt(0) as ViewGroup
        val childCount = tabLayout.childCount

        if (childCount != 0){
            val displayMetrics = context.resources.displayMetrics
            val tabMinWidth = displayMetrics.widthPixels / childCount // min width base on phone width

            for (i in 0 .. childCount){
                tabLayout.getChildAt(i).minimumWidth = tabMinWidth
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }
}