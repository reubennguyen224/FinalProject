package com.training.finalproject.home_activity.helpcenter

import android.view.View
import com.training.finalproject.databinding.FragmentHelpCenterBinding
import com.training.finalproject.home_activity.HomeActivity
import com.training.finalproject.utils.BaseFragment

class HelpCenterFragment : BaseFragment<FragmentHelpCenterBinding>(
    FragmentHelpCenterBinding::inflate
) {
    override fun setupView() {
        binding.toolbar.txtHeader.text = "Help Center"
        binding.toolbar.searchBar.visibility = View.GONE
        binding.toolbar.btnDrawer.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }
    }
}