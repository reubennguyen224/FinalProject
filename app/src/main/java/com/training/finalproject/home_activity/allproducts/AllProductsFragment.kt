package com.training.finalproject.home_activity.allproducts

import com.training.finalproject.databinding.FragmentAllProductsBinding
import com.training.finalproject.home_activity.HomeActivity
import com.training.finalproject.utils.BaseFragment

class AllProductsFragment : BaseFragment<FragmentAllProductsBinding>(
    FragmentAllProductsBinding::inflate
) {
    override fun setupView() {
        binding.toolbar.txtHeader.text = "All Product"
        binding.toolbar.btnDrawer.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }
    }

}