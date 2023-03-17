package com.training.finalproject.home_activity.becomeseller

import com.training.finalproject.databinding.FragmentBecomeSellerBinding
import com.training.finalproject.home_activity.HomeActivity
import com.training.finalproject.utils.BaseFragment

class BecomeSellerFragment : BaseFragment<FragmentBecomeSellerBinding>(
    FragmentBecomeSellerBinding::inflate
) {
    override fun setupView() {
        binding.toolbar.txtHeader.text = "Become Seller"
        binding.toolbar.btnDrawer.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }
    }


}