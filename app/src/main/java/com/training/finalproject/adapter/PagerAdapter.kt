package com.training.finalproject.adapter

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.training.finalproject.ui.shopping.ProductDetailsInformationFragment
import com.training.finalproject.ui.shopping.ReviewProductFragment
import com.training.finalproject.utils.CustomViewPager

class PagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    fun getViewAtPosition(position: Int): View? {
        return if (position == 0) {
            ProductDetailsInformationFragment().view
        } else {
            ReviewProductFragment().view
        }
    }

    fun getCurrent(): View? {
        return getItem(currentPosition).view
    }

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return if (position == 0)
            ProductDetailsInformationFragment()
        else
            ReviewProductFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) {
            "Details"
        } else {
            "Review"
        }
    }

    private var currentPosition = -1
    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        if (position != currentPosition) {
            val pager: CustomViewPager = container as CustomViewPager
            currentPosition = position
            getViewAtPosition(currentPosition)?.let { pager.measureCurrentView(it) }
        }
    }
}