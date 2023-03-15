package com.training.finalproject.ui.shopping

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.training.finalproject.R
import com.training.finalproject.adapter.PagerAdapter
import com.training.finalproject.databinding.FragmentProductDetailBinding
import com.training.finalproject.model.HomeRecyclerViewItem
import com.training.finalproject.utils.MyApplication
import com.training.finalproject.utils.replaceFragment
import com.training.finalproject.viewmodel.HomeFragmentViewModel

class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private var item: HomeRecyclerViewItem.Product? = null
    private var isFavourite = false
    private val viewModel by activityViewModels<HomeFragmentViewModel> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = Color.WHITE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        viewModel.chosenProduct.observe(viewLifecycleOwner){
            binding.txtNameProduct.text = it?.name
            binding.txtNameStore.text = it?.seller
            binding.txtPriceProduct.text = "$${it?.sale_price}"
            binding.txtOriginalPriceProduct.text = "$" + it?.price
            binding.ratingBar.rating = it?.star?.toFloat() ?: 0.0F
            Glide.with(requireContext()).load(it?.image).fitCenter().into(binding.imgProduct)
        }

        viewModel.cartListLiveData.observe(viewLifecycleOwner){
            var total = 0
            for (i in it) total += i.number
            binding.appBar.badgeCart.text = total.toString()
        }
        binding.appBar.txtHeaderApp.visibility = View.INVISIBLE

        viewModel.resetNumber()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = binding.tabLayoutProduct

        viewModel.numberProduct.observe(viewLifecycleOwner){
            if (it < 0 ) {
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
                    if (tab?.position == 0){
                        binding.pagerProduct.currentItem = 0
                    }
                    else binding.pagerProduct.currentItem = 1

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
                override fun onTabReselected(tab: TabLayout.Tab?) = Unit
            })
            setupWithViewPager(binding.pagerProduct)
        }
        val pagerAdapter = PagerAdapter(childFragmentManager)
        binding.pagerProduct.apply {
            adapter = pagerAdapter
            offscreenPageLimit = 2
        }

        binding.pagerProduct.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) = Unit

            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
//                val view = (binding.pagerProduct.adapter as PagerAdapter).getViewAtPosition(position)
//
//                view?.let {
//                    updateHeightPager(view = view, binding.pagerProduct)
//                }
            }

            override fun onPageScrollStateChanged(state: Int) = Unit

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
            val id = viewModel.chosenProduct.value?.id
            val number = binding.txtNumberProduct.text.toString().toInt()
            if (id != null) {
                viewModel.addToCart(id, number, (requireActivity().application as MyApplication).repository)
            }
            Toast.makeText(requireContext(), "Add to cart success!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()

        }

        binding.btnFavourite.setOnClickListener {
            if (isFavourite){
                isFavourite = false
                binding.btnFavourite.setImageResource(R.drawable.ic_checked_favorite)
            } else{
                isFavourite = true
                binding.btnFavourite.setImageResource(R.drawable.ic_uncheck_favorite)
            }
        }

        binding.appBar.btnCart.setOnClickListener {
            replaceFragment(CartFragment(), R.id.fragmentContainer, true)
        }
    }

    fun updateHeightPager(view: View, pager: ViewPager){
        view.post {
            val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)

            if (pager.layoutParams.height != view.measuredHeight){
                pager.layoutParams = (pager.layoutParams).also {
                    it.height = view.measuredHeight
                }
            }
        }
    }
}