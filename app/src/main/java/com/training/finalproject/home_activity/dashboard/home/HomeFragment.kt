package com.training.finalproject.home_activity.dashboard.home

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.training.finalproject.R
import com.training.finalproject.databinding.FragmentHomeBinding
import com.training.finalproject.home_activity.HomeActivity
import com.training.finalproject.home_activity.dashboard.home.adapter.HomeFragmentAdapter
import com.training.finalproject.home_activity.dashboard.shopping.cart.CartFragment
import com.training.finalproject.home_activity.dashboard.shopping.cart.viewmodel.CartViewModel
import com.training.finalproject.home_activity.dashboard.shopping.detail_product.presenter.ProductDetailFragment
import com.training.finalproject.utils.BaseFragment
import com.training.finalproject.utils.ItemSpanSizeLookup
import com.training.finalproject.utils.NewProductDecoration
import com.training.finalproject.utils.replaceFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
    private val viewModel: HomeFragmentViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels { CartViewModel.Factory }
    private val homeAdapter = HomeFragmentAdapter()
    private lateinit var gridLayoutManager: GridLayoutManager
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartViewModel.getNumberCart()

        retainInstance = true

        homeAdapter.onProductClick = { product ->
            val detailFragment = ProductDetailFragment()
            val bundle = Bundle()
            bundle.putInt("productID", product.id)
            detailFragment.arguments = bundle

            replaceFragment(
                detailFragment, R.id.fragmentContainer,
                addToStack = true,
                callToActivity = true
            )

        } // event click 1 product

        if (firstTimeCreated(savedInstanceState))
            viewModel.getData()

        gridLayoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)


    }

    override fun setupView() {
        activity?.window?.statusBarColor =
            resources.getColor(R.color.colorStatus) // set color status bar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.btnDrawer.setOnClickListener {
            (activity as HomeActivity).openDrawer() //open drawer
        }

        viewModel.homeListLiveData.observe(viewLifecycleOwner) { result ->
            binding.productListContainer.isRefreshing = false
            homeAdapter.diff.submitList(result)
        } //observe home list to display

        cartViewModel.countLiveData.observe(viewLifecycleOwner) {
            if (it == 0) {
                viewModel.getCart()
                binding.toolbar.badgeCart.visibility = View.INVISIBLE
            } else binding.toolbar.badgeCart.visibility = View.VISIBLE
            binding.toolbar.badgeCart.text = it.toString()
        } // set badge of cart button on toolbar

        binding.productList.apply {
            homeAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT
            adapter = homeAdapter
            gridLayoutManager.spanSizeLookup = ItemSpanSizeLookup(homeAdapter)
            val itemDecoration = NewProductDecoration(26)
            addItemDecoration(itemDecoration)
            layoutManager = gridLayoutManager
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

    private var recyclerViewState: Parcelable? = null
    override fun onPause() {
        super.onPause()
        recyclerViewState = gridLayoutManager.onSaveInstanceState()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        recyclerViewState = gridLayoutManager.onSaveInstanceState()
        savedInstanceState.putParcelable(
            "recyclerState",
            recyclerViewState
        ) // save scrolling position of recyclerview
    }

    override fun onResume() {
        super.onResume()
        gridLayoutManager.onRestoreInstanceState(recyclerViewState)
        binding.productList.layoutManager = gridLayoutManager
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            recyclerViewState = savedInstanceState.getParcelable("recyclerState")
        }

    }

}