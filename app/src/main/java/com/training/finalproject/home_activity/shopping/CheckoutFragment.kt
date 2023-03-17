package com.training.finalproject.home_activity.shopping

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.finalproject.R
import com.training.finalproject.SuccessDialogFragment
import com.training.finalproject.home_activity.shopping.cart.adapter.CheckoutAdapter
import com.training.finalproject.databinding.FragmentCheckoutBinding
import com.training.finalproject.home_activity.dashboard.DashboardFragment
import com.training.finalproject.home_activity.shopping.cart.model.CartItem
import com.training.finalproject.home_activity.shopping.cart.CartViewModel
import com.training.finalproject.utils.ProductDecoration
import com.training.finalproject.utils.replaceFragment
import com.training.finalproject.viewmodel.HomeFragmentViewModel
import kotlin.math.roundToInt


class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by activityViewModels { CartViewModel.Factory }
    private val homeViewModel by activityViewModels<HomeFragmentViewModel> {HomeFragmentViewModel.Factory}
    private val productAdapter = CheckoutAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = Color.WHITE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        binding.appBarCheckout.btnCart.visibility = View.INVISIBLE
        binding.appBarCheckout.txtHeaderApp.text = getString(R.string.checkout)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBarCheckout.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.txtShippingAddress.text = "Domen Tikoro Street:  825 Baker Avenue, Dallas,\n" +
                "Texas, Zip code  75202"

        viewModel.chosenItemCartList.observe(viewLifecycleOwner) {
            productAdapter.diff.submitList(it)
            updateLayout(it!!)
        }

        binding.appBarCheckout.badgeCart.visibility = View.INVISIBLE

        binding.listSummary.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(
                binding.listSummary.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
            val itemDeco = ProductDecoration(20)
            addItemDecoration(itemDeco)
        }

        binding.btnBuy.setOnClickListener {
            viewModel.checkoutCart()
            homeViewModel.getCart()
            replaceFragment(DashboardFragment(), R.id.fragmentContainer, true)
            SuccessDialogFragment().show(
                parentFragmentManager,
                SuccessDialogFragment::class.java.simpleName
            )
        }
    }

    private fun updateLayout(list: ArrayList<CartItem>) {
        var sum = 0.0
        for (i in list) {
            if (i.product != null)
                sum += i.number * i.product.sale_price
        }
        val subtotal = (sum * 100.0).roundToInt() / 100.0
        binding.txtSubtotal.text = "$" + subtotal.toString()
        binding.txtTax.text = "$1,00"
        binding.txtDeliveryFee.text = "$4,00"
        binding.txtTotalPrice.text = "$${subtotal + 5}"
    }
}