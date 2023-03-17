package com.training.finalproject.home_activity.shopping.cart

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.finalproject.R
import com.training.finalproject.databinding.FragmentCartBinding
import com.training.finalproject.home_activity.shopping.CheckoutFragment
import com.training.finalproject.home_activity.shopping.cart.adapter.CartAdapter
import com.training.finalproject.home_activity.shopping.cart.model.CartItem
import com.training.finalproject.utils.ProductDecoration
import com.training.finalproject.utils.replaceFragment
import com.training.finalproject.viewmodel.HomeFragmentViewModel
import kotlin.math.roundToInt


class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeFragmentViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels{CartViewModel.Factory}
    private val cartAdapter = CartAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = Color.WHITE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)

        cartViewModel.setCartList(viewModel.cartList)

        binding.toolbar.btnCart.visibility = View.INVISIBLE
//        viewModel.getCart()

        cartViewModel.cartListLiveData.observe(viewLifecycleOwner) { cartList ->
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
            } else number--
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.listCartItem.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(
                binding.listCartItem.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
            addItemDecoration(ProductDecoration(10))
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
            }
        }
    }
}