package com.training.finalproject.home_activity.dashboard.shopping.cart.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.training.finalproject.data.AppRepository
import com.training.finalproject.home_activity.dashboard.shopping.cart.model.Cart
import com.training.finalproject.home_activity.dashboard.shopping.cart.model.CartItem
import com.training.finalproject.utils.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: AppRepository, private val savedStateHandle: SavedStateHandle):ViewModel() {

    private val cartList = ArrayList<CartItem>()
    val cartListLiveData = MutableLiveData<ArrayList<CartItem>>()
    private val cartRoomList = ArrayList<Cart>()
    val chosenItemCartList = MutableLiveData<ArrayList<CartItem>?>()

    fun saveChosenItem(list: ArrayList<CartItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            chosenItemCartList.postValue(null)
            chosenItemCartList.postValue(list)
        }
    }

    fun checkoutCart() {
        viewModelScope.launch(Dispatchers.IO) {
            val chosenItem = chosenItemCartList.value ?: ArrayList()
            val checkoutCart = ArrayList<Cart>()
            for (i in chosenItem) {
                for (j in cartRoomList)
                    if (i.product?.id == j.productID)
                        checkoutCart.add(j)
            }
            cartRepository.deleteCart(checkoutCart.toTypedArray())
        }
    }

    fun setCartList(list: List<CartItem>) = viewModelScope.launch(Dispatchers.IO){

        cartList.clear()
        cartList.addAll(list)
        val list = cartRepository.getAllCart()
        cartRoomList.clear()
        cartRoomList.addAll(list)
        cartListLiveData.postValue(cartList)
    }

    fun deleteItem(item: CartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val delete = ArrayList<Cart>()
            for (i in cartRoomList)
                if (item.product?.id == i.productID) {
                    delete.add(i)
                }
            cartRepository.deleteCart(delete.toTypedArray())
            cartList.remove(item)
            cartListLiveData.postValue(cartList)
        }

    }

    fun updateItemCart(position: Int, isAdd: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val product = cartList[position].product
            var number = cartList[position].number
            if (isAdd) {
                number += 1
            } else {
                number -= 1
            }
            cartRepository.updateCart(product, number)
            cartList[position].number = number
            cartListLiveData.postValue(cartList)
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()
                return CartViewModel((application as MyApplication).repository, savedStateHandle) as T
            }
        }
    }
}