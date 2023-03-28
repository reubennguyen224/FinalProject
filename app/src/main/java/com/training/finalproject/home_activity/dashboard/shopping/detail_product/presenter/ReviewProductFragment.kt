package com.training.finalproject.home_activity.dashboard.shopping.detail_product.presenter

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.finalproject.databinding.FragmentReviewProductBinding
import com.training.finalproject.home_activity.dashboard.shopping.detail_product.adapter.ReviewAdapter
import com.training.finalproject.home_activity.dashboard.shopping.detail_product.viewmodel.ReviewViewModel
import com.training.finalproject.utils.BaseFragment
import com.training.finalproject.utils.Status

class ReviewProductFragment : BaseFragment<FragmentReviewProductBinding>(
    FragmentReviewProductBinding::inflate
) {

    private val viewModel: ReviewViewModel by viewModels { ReviewViewModel.Factory }
    private val reviewAdapter = ReviewAdapter()

    override fun setupView() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getReviewList().observe(viewLifecycleOwner){
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        resource.data?.let { list -> reviewAdapter.diff.submitList(list) }
                    }
                    Status.LOADING -> {
                        Toast.makeText(requireContext(), "Wait a minutes", Toast.LENGTH_SHORT).show()
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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