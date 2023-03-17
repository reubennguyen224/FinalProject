package com.training.finalproject.home_activity.dashboard.transaction

import android.view.View
import com.training.finalproject.R
import com.training.finalproject.databinding.FragmentTransactionBinding
import com.training.finalproject.home_activity.HomeActivity
import com.training.finalproject.home_activity.dashboard.shopping.cart.CartFragment
import com.training.finalproject.utils.BaseFragment
import com.training.finalproject.utils.replaceFragment


class TransactionFragment : BaseFragment<FragmentTransactionBinding>(
    FragmentTransactionBinding::inflate
) {

    override fun setupView() {
        binding.toolbar.btnDrawer.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }
        binding.toolbar.txtHeader.text = "Transaction"
        binding.toolbar.searchBar.visibility = View.GONE
        binding.toolbar.btnCart.setOnClickListener {
            replaceFragment(
                CartFragment(), R.id.fragmentContainer,
                addToStack = true,
                callToActivity = true
            )
        }
    }
}