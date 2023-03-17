package com.training.finalproject.home_activity.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.training.finalproject.home_activity.HomeActivity
import com.training.finalproject.R
import com.training.finalproject.databinding.FragmentFeedsBinding
import com.training.finalproject.home_activity.shopping.cart.CartFragment
import com.training.finalproject.utils.replaceFragment

class FeedsFragment : Fragment() {
    private lateinit var binding: FragmentFeedsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedsBinding.inflate(inflater, container, false)
        binding.toolbar.txtHeader.text = "Feeds"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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