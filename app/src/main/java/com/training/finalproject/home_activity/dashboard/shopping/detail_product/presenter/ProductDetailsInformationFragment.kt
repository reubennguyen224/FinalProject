package com.training.finalproject.home_activity.dashboard.shopping.detail_product.presenter

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.training.finalproject.databinding.FragmentProductDetailsInformationBinding
import com.training.finalproject.home_activity.dashboard.shopping.detail_product.adapter.ColorAdapter
import com.training.finalproject.home_activity.dashboard.shopping.detail_product.viewmodel.ProductDetailInformationViewModel
import com.training.finalproject.utils.BaseFragment

class ProductDetailsInformationFragment : BaseFragment<FragmentProductDetailsInformationBinding>(
    FragmentProductDetailsInformationBinding::inflate
) {
    private val viewModel: ProductDetailInformationViewModel by viewModels(ownerProducer = {requireParentFragment()})

    private val adapterList = ColorAdapter()

    override fun setupView() {
        viewModel.chosenProduct.observe(viewLifecycleOwner) {
            binding.txtDescriptionProduct.text = it?.description
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val itemDecoration = object : ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top = 5
                outRect.left = 5
                outRect.right = 2
                outRect.bottom = 5
            }
        }
        val colorArrayList = arrayListOf("Red", "Silver", "Black")
        adapterList.diff.submitList(colorArrayList)
        binding.listColor.apply {
            adapter = adapterList
            layoutManager = LinearLayoutManager(
                binding.listColor.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            addItemDecoration(itemDecoration)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}