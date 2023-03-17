package com.training.finalproject.home_activity.newrelease

import com.training.finalproject.databinding.FragmentNewReleaseBinding
import com.training.finalproject.home_activity.HomeActivity
import com.training.finalproject.utils.BaseFragment

class NewReleaseFragment : BaseFragment<FragmentNewReleaseBinding>(
    FragmentNewReleaseBinding::inflate
) {

    override fun setupView() {

        binding.toolbar.txtHeader.text = "New Release"
        binding.toolbar.btnDrawer.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }
    }
}