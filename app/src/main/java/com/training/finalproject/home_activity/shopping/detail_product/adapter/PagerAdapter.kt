package com.training.finalproject.home_activity.shopping.detail_product.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.training.finalproject.home_activity.shopping.detail_product.presenter.ProductDetailsInformationFragment
import com.training.finalproject.home_activity.shopping.detail_product.presenter.ReviewProductFragment

class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    fun getViewAtPosition(position: Int): View? {
        return if (position == 0) {
            ProductDetailsInformationFragment().view
        } else {
            ReviewProductFragment().view
        }
    }

    override fun getItemCount(): Int  = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
            ProductDetailsInformationFragment()
        else
            ReviewProductFragment()
    }
}