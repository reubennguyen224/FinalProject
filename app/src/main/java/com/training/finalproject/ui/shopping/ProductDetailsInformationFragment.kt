package com.training.finalproject.ui.shopping

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.training.finalproject.adapter.ColorAdapter
import com.training.finalproject.databinding.FragmentProductDetailsInformationBinding
import com.training.finalproject.viewmodel.HomeFragmentViewModel

class ProductDetailsInformationFragment : Fragment() {
    private var _binding: FragmentProductDetailsInformationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeFragmentViewModel by activityViewModels()

    private val adapterList = ColorAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.chosenProduct.observe(viewLifecycleOwner) {
            binding.txtDescriptionProduct.text = it?.description
        }

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