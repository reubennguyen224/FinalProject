package com.training.finalproject.home_activity.dashboard.shopping.detail_product.presenter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.finalproject.databinding.FragmentReviewProductBinding
import com.training.finalproject.home_activity.dashboard.shopping.detail_product.adapter.ReviewAdapter
import com.training.finalproject.home_activity.dashboard.shopping.detail_product.viewmodel.ReviewViewModel
import com.training.finalproject.utils.BaseFragment

class ReviewProductFragment : BaseFragment<FragmentReviewProductBinding>(
    FragmentReviewProductBinding::inflate
) {

    private val viewModel: ReviewViewModel by viewModels { ReviewViewModel.Factory }
    private val reviewAdapter = ReviewAdapter()

    override fun setupView() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getReviewList()
        viewModel.reviewListLiveData.observe(viewLifecycleOwner) {
            reviewAdapter.diff.submitList(it)
        }
        val reviewLayoutManager = object :
            LinearLayoutManager(binding.listReview.context, VERTICAL, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }


        binding.listReview.apply {
            adapter = reviewAdapter
            layoutManager = reviewLayoutManager
            setHasFixedSize(false)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}