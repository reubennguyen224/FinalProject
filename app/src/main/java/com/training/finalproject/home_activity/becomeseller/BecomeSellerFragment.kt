package com.training.finalproject.home_activity.becomeseller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.training.finalproject.R
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