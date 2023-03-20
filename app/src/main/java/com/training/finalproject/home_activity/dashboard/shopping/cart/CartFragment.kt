package com.training.finalproject.home_activity.dashboard.shopping.cart

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.training.finalproject.R
import com.training.finalproject.databinding.FragmentCartBinding
import com.training.finalproject.home_activity.dashboard.home.HomeFragmentViewModel
import com.training.finalproject.home_activity.dashboard.shopping.CheckoutFragment
import com.training.finalproject.home_activity.dashboard.shopping.cart.adapter.CartAdapter
import com.training.finalproject.home_activity.dashboard.shopping.cart.model.CartItem
import com.training.finalproject.home_activity.dashboard.shopping.cart.viewmodel.CartViewModel
import com.training.finalproject.utils.BaseFragment
import com.training.finalproject.utils.CartDecoration
import com.training.finalproject.utils.replaceFragment
import kotlin.math.roundToInt


class CartFragment : BaseFragment<FragmentCartBinding>(
    FragmentCartBinding::inflate
) {
    private val viewModel: HomeFragmentViewModel by activityViewModels{HomeFragmentViewModel.Factory}
    private val cartViewModel: CartViewModel by activityViewModels { CartViewModel.Factory }
    private val cartAdapter = CartAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = Color.WHITE

    }

    override fun setupView() {
        cartViewModel.setCartList(viewModel.cartList)
        cartViewModel.getSaveState()
        binding.toolbar.btnCart.visibility = View.INVISIBLE

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        cartViewModel.saveStateHandle(cartAdapter.diff.currentList)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartViewModel.cartListLiveData.observe(viewLifecycleOwner) { cartList ->
            var checked = false
            for (i in cartList) if (i.checked) checked = true
            if (checked)
                binding.chkSelectAll.isChecked = true
            updateFooter(cartList)
            cartAdapter.diff.submitList(cartList)
        }

        cartAdapter.onCheckClick = { position ->
            val list = cartAdapter.diff.currentList.toMutableList()
            val oldItem = list[position]
            val newItem = CartItem(oldItem.id, oldItem.product, oldItem.number, !oldItem.checked)
            list[position] = newItem
            cartAdapter.diff.submitList(list.toList())
            updateFooter(list)
        }

        cartAdapter.onUpdateNumberClick = { isAdd, position ->
            cartViewModel.updateItemCart(position, isAdd)
            val list = cartAdapter.diff.currentList.toMutableList()
            val oldItem = list[position]
            var number = oldItem.number
            number = if (isAdd) {
                number++
            } else {
                number--
            }
            val newItem = CartItem(oldItem.id, oldItem.product, number, oldItem.checked)
            list[position] = newItem
            cartAdapter.diff.submitList(list)
            updateFooter(list)
        }

        binding.toolbar.badgeCart.visibility = View.INVISIBLE

        cartAdapter.onDeleteClick = { item ->
            val list = cartAdapter.diff.currentList.toMutableList()
            list.remove(item)
            cartViewModel.deleteItem(item)
            viewModel.cartList.remove(item)
            viewModel.setCartValue()
            cartAdapter.diff.submitList(viewModel.cartList.toList())
            updateFooter(list)
        }

        binding.chkSelectAll.setOnCheckedChangeListener { _, isChecked ->
            val list = viewModel.cartList
            for (item in list) item.checked = false
            for (item in list) item.checked = isChecked
            cartAdapter.diff.submitList(list.toList())
            binding.listCartItem.adapter = cartAdapter
            updateFooter(list)
        }

        binding.toolbar.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.listCartItem.apply {
            cartAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(
                binding.listCartItem.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
            addItemDecoration(CartDecoration(20))
        }

    }

    private fun updateFooter(list: List<CartItem>) {
        val tempList = list.filter {
            it.checked
        }
        var sum = 0.0
        for (i in tempList.indices) {
            if (tempList[i].product != null)
                sum += (tempList[i].product?.sale_price ?: 0.0) * tempList[i].number
        }
        val totalPrice = (sum * 100.0).roundToInt() / 100.0
        binding.txtTotalPrice.text = totalPrice.toString()
        binding.btnBuy.text = "Buy(${tempList.size} item)"
        binding.btnBuy.setOnClickListener {
            if (tempList.isNotEmpty()) {
                cartViewModel.saveChosenItem(ArrayList(tempList))
                replaceFragment(CheckoutFragment(), R.id.fragmentContainer, true)
            } else{
                Toast.makeText(requireContext(), "Choose at least 1 product to continue", Toast.LENGTH_SHORT).show()
            }
        }
    }
}