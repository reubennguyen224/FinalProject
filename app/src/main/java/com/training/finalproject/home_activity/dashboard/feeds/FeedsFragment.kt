package com.training.finalproject.home_activity.dashboard.feeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.training.finalproject.home_activity.HomeActivity
import com.training.finalproject.R
import com.training.finalproject.databinding.FragmentFeedsBinding
import com.training.finalproject.home_activity.dashboard.shopping.cart.CartFragment
import com.training.finalproject.utils.BaseFragment
import com.training.finalproject.utils.replaceFragment

class FeedsFragment : BaseFragment<FragmentFeedsBinding>(
    FragmentFeedsBinding::inflate
) {

    override fun setupView() {
        binding.toolbar.btnDrawer.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }
        binding.toolbar.txtHeader.text = "Feeds"
        binding.toolbar.btnCart.setOnClickListener {
            replaceFragment(
                CartFragment(), R.id.fragmentContainer,
                addToStack = true,
                callToActivity = true
            )
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}