package com.training.finalproject.home_activity.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.training.finalproject.R
import com.training.finalproject.databinding.FragmentSettingsBinding
import com.training.finalproject.home_activity.HomeActivity
import com.training.finalproject.utils.BaseFragment


class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {
    override fun setupView() {
        binding.toolbar.txtHeader.text = "Settings"
        binding.toolbar.searchBar.visibility = View.GONE
        binding.toolbar.btnDrawer.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }
    }
}