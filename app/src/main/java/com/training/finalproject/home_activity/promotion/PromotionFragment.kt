package com.training.finalproject.home_activity.promotion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.training.finalproject.R
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