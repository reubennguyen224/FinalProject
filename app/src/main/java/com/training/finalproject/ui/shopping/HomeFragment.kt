package com.training.finalproject.ui.shopping

import android.content.res.Resources.Theme
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.OnScrollListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.ThemeCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.training.finalproject.HomeActivity
import com.training.finalproject.R
import com.training.finalproject.adapter.HomeFragmentAdapter
import com.training.finalproject.databinding.FragmentHomeBinding
import com.training.finalproject.model.HomeRecyclerViewItem
import com.training.finalproject.utils.*
import com.training.finalproject.viewmodel.HomeFragmentViewModel
import kotlinx.coroutines.delay

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeFragmentViewModel by activityViewModels()

    private val homeAdapter = HomeFragmentAdapter()
    var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.statusBarColor = resources.getColor(R.color.colorStatus)
        if (_binding == null)
            _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.toolbar.btnDrawer.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }

        homeAdapter.onProductClick  = { position ->
            val item = homeAdapter.diff.currentList[position] as HomeRecyclerViewItem.Product
            val detailFragment = ProductDetailFragment()

            viewModel.getItem(id = item.id)

            replaceFragment(detailFragment, R.id.fragmentContainer,
                addToStack = true,
                callToActivity = true
            )

        }

        viewModel.homeListLiveData.observe(viewLifecycleOwner) { result ->
            homeAdapter.diff.submitList(result)
        }
        viewModel.cartListLiveData.observe(viewLifecycleOwner){
            if (it.isEmpty() ){
                viewModel.getCart((activity?.application as MyApplication).repository)
                binding.toolbar.badgeCart.visibility = View.INVISIBLE
            } else binding.toolbar.badgeCart.visibility = View.VISIBLE
            var total = 0
            for(i in viewModel.cartList) total += i.number
            binding.toolbar.badgeCart.text = total.toString()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            binding.productListContainer.isRefreshing = false
        }

        binding.toolbar.btnCart.setOnClickListener {
            replaceFragment(CartFragment(), R.id.fragmentContainer,
                addToStack = true,
                callToActivity = true
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val recyclerViewState = binding.productList.layoutManager?.onSaveInstanceState()
        outState.putParcelable("recyclerState", recyclerViewState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null){
            val recyclerViewState: Parcelable? = savedInstanceState.getParcelable("recyclerState")
            binding.productList.layoutManager?.onRestoreInstanceState(recyclerViewState)
        }

    }
}