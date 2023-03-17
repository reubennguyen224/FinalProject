package com.training.finalproject.home_activity.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.training.finalproject.R
import com.training.finalproject.databinding.FragmentCategoriesBinding
import com.training.finalproject.home_activity.HomeActivity
import com.training.finalproject.utils.BaseFragment

class CategoriesFragment : BaseFragment<FragmentCategoriesBinding>(
    FragmentCategoriesBinding::inflate
) {
    override fun setupView() {
        binding.toolbar.txtHeader.text = "Categories"
        binding.toolbar.btnDrawer.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }
    }


}