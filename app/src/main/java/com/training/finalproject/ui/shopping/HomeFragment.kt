package com.training.finalproject.ui.shopping

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.training.finalproject.HomeActivity
import com.training.finalproject.R
import com.training.finalproject.adapter.HomeFragmentAdapter
import com.training.finalproject.databinding.FragmentHomeBinding
import com.training.finalproject.model.HomeRecyclerViewItem
import com.training.finalproject.utils.ItemSpanSizeLookup
import com.training.finalproject.utils.MyApplication
import com.training.finalproject.utils.NewProductDecoration
import com.training.finalproject.utils.replaceFragment
import com.training.finalproject.viewmodel.HomeFragmentViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeFragmentViewModel by activityViewModels()

    private val homeAdapter = HomeFragmentAdapter()
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.statusBarColor = resources.getColor(R.color.colorStatus) // set color status bar
        if (_binding == null)
            _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.toolbar.btnDrawer.setOnClickListener {
            (activity as HomeActivity).openDrawer() //open drawer
        }

        homeAdapter.onProductClick = { position ->
            val item = homeAdapter.diff.currentList[position] as HomeRecyclerViewItem.Product
            val detailFragment = ProductDetailFragment()

            viewModel.getItem(id = item.id)

            replaceFragment(
                detailFragment, R.id.fragmentContainer,
                addToStack = true,
                callToActivity = true
            )

        } // event click 1 product

        viewModel.homeListLiveData.observe(viewLifecycleOwner) { result ->
            binding.productListContainer.isRefreshing = false
            homeAdapter.diff.submitList(result)

        } //observe home list to display

        viewModel.cartListLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                viewModel.getCart((activity?.application as MyApplication).repository)
                binding.toolbar.badgeCart.visibility = View.INVISIBLE
            } else binding.toolbar.badgeCart.visibility = View.VISIBLE
            var total = 0
            for (i in viewModel.cartList) total += i.number
            binding.toolbar.badgeCart.text = total.toString()
        } // set badge of cart button on toolbar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHomeList()
        binding.productList.apply {
            homeAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT
            adapter = homeAdapter
            val mLayoutManager =
                GridLayoutManager(binding.productList.context, 2, GridLayoutManager.VERTICAL, false)
            mLayoutManager.spanSizeLookup = ItemSpanSizeLookup(homeAdapter)
            val itemDecoration = NewProductDecoration(26)
            addItemDecoration(itemDecoration)
            layoutManager = mLayoutManager
            setHasFixedSize(true)

        }
        binding.productListContainer.setOnRefreshListener {
            binding.productListContainer.isRefreshing = true
            viewModel.getHomeList()

        }

        binding.toolbar.btnCart.setOnClickListener {
            replaceFragment(
                CartFragment(), R.id.fragmentContainer,
                addToStack = true,
                callToActivity = true
            )
        } //event click cart button

        binding.productListContainer.setColorSchemeColors(
            resources.getColor(android.R.color.holo_blue_bright),
            resources.getColor(android.R.color.holo_green_light),
            resources.getColor(android.R.color.holo_orange_light),
            resources.getColor(android.R.color.holo_red_light)
        ) // color of progress bar appear when pull-to-refresh
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val recyclerViewState = binding.productList.layoutManager?.onSaveInstanceState()
        outState.putParcelable("recyclerState", recyclerViewState) // save scrolling position of recyclerview
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            val recyclerViewState: Parcelable? = savedInstanceState.getParcelable("recyclerState")
            binding.productList.layoutManager?.onRestoreInstanceState(recyclerViewState) // get scrolling position of recycler view
        }

    }
}