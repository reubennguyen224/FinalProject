package com.training.finalproject.home_activity.dashboard.myprofile

import android.os.BaseBundle
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.training.finalproject.R
import com.training.finalproject.databinding.FragmentMyProfileBinding
import com.training.finalproject.home_activity.HomeActivity
import com.training.finalproject.home_activity.dashboard.shopping.cart.CartFragment
import com.training.finalproject.utils.BaseFragment
import com.training.finalproject.utils.replaceFragment

class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(
    FragmentMyProfileBinding::inflate
) {

    override fun setupView() {
        binding.toolbar.txtHeader.text = "Profile"
        binding.toolbar.searchBar.visibility = View.GONE
        binding.toolbar.btnDrawer.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }
        binding.toolbar.btnCart.setOnClickListener {
            replaceFragment(
                CartFragment(), R.id.fragmentContainer,
                addToStack = true,
                callToActivity = true
            )
        }
    }
}