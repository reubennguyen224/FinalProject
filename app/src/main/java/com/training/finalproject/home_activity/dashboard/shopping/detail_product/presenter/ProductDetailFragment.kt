package com.training.finalproject.home_activity.dashboard.shopping.detail_product.presenter

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.training.finalproject.R
import com.training.finalproject.databinding.FragmentProductDetailBinding
import com.training.finalproject.home_activity.dashboard.home.HomeFragmentViewModel
import com.training.finalproject.home_activity.dashboard.shopping.cart.CartFragment
import com.training.finalproject.home_activity.dashboard.shopping.cart.viewmodel.CartViewModel
import com.training.finalproject.home_activity.dashboard.shopping.detail_product.adapter.PagerAdapter
import com.training.finalproject.home_activity.dashboard.shopping.detail_product.viewmodel.ProductDetailInformationViewModel
import com.training.finalproject.model.HomeRecyclerViewItem
import com.training.finalproject.utils.BaseFragment
import com.training.finalproject.utils.replaceFragment
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding>(
    FragmentProductDetailBinding::inflate
) {
    private var item: HomeRecyclerViewItem.Product? = null
    private var isFavourite = false
    private val homeViewModel by activityViewModels<HomeFragmentViewModel>()
    private val cartViewModel by activityViewModels<CartViewModel> { CartViewModel.Factory }
    private val viewModel by viewModels<ProductDetailInformationViewModel> { ProductDetailInformationViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = Color.WHITE
        val id = arguments?.getInt("productID", 0) ?: 0
        viewModel.getItem(id)
    }

    override fun setupView() {
        viewModel.chosenProduct.observe(viewLifecycleOwner) {
            binding.txtNameProduct.text = it?.name
            binding.txtNameStore.text = it?.seller
            binding.txtPriceProduct.text = "$${it?.sale_price}"
            binding.txtOriginalPriceProduct.text = "$" + it?.price
            binding.ratingBar.rating = it?.star?.toFloat() ?: 0.0F
            Glide.with(requireContext()).load(it?.image).fitCenter().into(binding.imgProduct)
        }
        cartViewModel.countLiveData.observe(viewLifecycleOwner) {
            binding.appBar.badgeCart.text = it.toString()
        }
        binding.appBar.txtHeaderApp.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        cartViewModel.getNumberCart()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null)
            viewModel.resetNumber()

        val tabLayout = binding.tabLayoutProduct

        viewModel.numberProduct.observe(viewLifecycleOwner) {
            if (it < 0) {
                Toast.makeText(requireContext(), "Out of stock", Toast.LENGTH_SHORT).show()
                binding.txtNumberProduct.text = "0"
            }
            binding.txtNumberProduct.text = it.toString()
        }

        tabLayout.apply {
            addTab(tabLayout.newTab().setText("Details"), true)
            addTab(tabLayout.newTab().setText("Review"))
            addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab?.position == 0) {
                        binding.pagerProduct.currentItem = 0
                    } else binding.pagerProduct.currentItem = 1

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
                override fun onTabReselected(tab: TabLayout.Tab?) = Unit
            })
        }
        val pagerAdapter = PagerAdapter(childFragmentManager, lifecycle)
        binding.pagerProduct.apply {
            adapter = pagerAdapter
            offscreenPageLimit = 2
        }

        binding.pagerProduct.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }

        })

        binding.appBar.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnAddProduct.setOnClickListener {
            viewModel.changeNumber(true)
        }

        binding.btnSubtractProduct.setOnClickListener {
            viewModel.changeNumber(false)
        }

        binding.btnAddToCart.setOnClickListener {
            val product = viewModel.chosenProduct.value
            val number = binding.txtNumberProduct.text.toString().toInt()
            if (product != null) {
                viewModel.addToCart(product, number, homeViewModel.userAccount)
            }
            cartViewModel.getNumberCart()
            lifecycleScope.launch {
                delay(5000)
                Toast.makeText(requireContext(), "Add to cart success!", Toast.LENGTH_SHORT).show()
            }


        }

        binding.btnFavourite.setOnClickListener {
            if (isFavourite) {
                isFavourite = false
                binding.btnFavourite.setImageResource(R.drawable.ic_checked_favorite)
            } else {
                isFavourite = true
                binding.btnFavourite.setImageResource(R.drawable.ic_uncheck_favorite)
            }
        }

        binding.appBar.btnCart.setOnClickListener {
            replaceFragment(CartFragment(), R.id.fragmentContainer, true)
        }
    }
}