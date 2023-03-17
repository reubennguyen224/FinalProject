package com.training.finalproject.home_activity.shopping.detail_product.presenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.finalproject.databinding.FragmentReviewProductBinding
import com.training.finalproject.home_activity.shopping.detail_product.adapter.ReviewAdapter
import com.training.finalproject.home_activity.shopping.detail_product.viewmodel.ReviewViewModel

class ReviewProductFragment : Fragment() {

    private var _binding: FragmentReviewProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReviewViewModel by viewModels()
    private val reviewAdapter = ReviewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewProductBinding.inflate(inflater, container, false)
        viewModel.reviewListLiveData.observe(viewLifecycleOwner) {
            reviewAdapter.diff.submitList(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getReviewList()

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