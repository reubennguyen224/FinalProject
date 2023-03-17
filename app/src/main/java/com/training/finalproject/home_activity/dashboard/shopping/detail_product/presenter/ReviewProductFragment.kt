package com.training.finalproject.home_activity.dashboard.shopping.detail_product.presenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.finalproject.databinding.FragmentReviewProductBinding
import com.training.finalproject.home_activity.dashboard.shopping.detail_product.adapter.ReviewAdapter
import com.training.finalproject.home_activity.dashboard.shopping.detail_product.viewmodel.ReviewViewModel
import com.training.finalproject.utils.BaseFragment

class ReviewProductFragment : BaseFragment<FragmentReviewProductBinding>(
    FragmentReviewProductBinding::inflate
) {

    private val viewModel: ReviewViewModel by viewModels()
    private val reviewAdapter = ReviewAdapter()

    override fun setupView() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getReviewList()
        viewModel.reviewListLiveData.observe(viewLifecycleOwner) {
            reviewAdapter.diff.submitList(it)
        }
        binding.listReview.apply {
            adapter = reviewAdapter
            layoutManager =
                LinearLayoutManager(binding.listReview.context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(false)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}