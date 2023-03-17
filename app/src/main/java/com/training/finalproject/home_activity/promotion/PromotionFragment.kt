package com.training.finalproject.home_activity.promotion

import com.training.finalproject.databinding.FragmentPromotionBinding
import com.training.finalproject.home_activity.HomeActivity
import com.training.finalproject.utils.BaseFragment

class PromotionFragment : BaseFragment<FragmentPromotionBinding>(
    FragmentPromotionBinding::inflate
) {
    override fun setupView() {
        binding.toolbar.txtHeader.text = "Promotion"
        binding.toolbar.btnDrawer.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }
    }
}