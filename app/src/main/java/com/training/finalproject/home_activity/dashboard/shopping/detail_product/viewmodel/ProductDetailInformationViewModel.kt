package com.training.finalproject.home_activity.dashboard.shopping.detail_product.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.training.finalproject.data.AppRepository
import com.training.finalproject.data.getAPI
import com.training.finalproject.model.HomeRecyclerViewItem
import com.training.finalproject.model.User
import com.training.finalproject.utils.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailInformationViewModel(val repository: AppRepository) : ViewModel() {
    val chosenProduct = MutableLiveData<HomeRecyclerViewItem.Product?>()

    fun resetNumber() = viewModelScope.launch(Dispatchers.IO) {
        numberProduct.postValue(1)
    }

    val numberProduct = MutableLiveData<Int>()

    fun changeNumber(state: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        if (state) {
            val number = numberProduct.value
            if (number != null) {
                numberProduct.postValue(number + 1)
            }
        } else {
            val number = numberProduct.value
            if (number != null && number > 0) {
                numberProduct.postValue(number - 1)
            }
        }
    }

    fun addToCart(product: HomeRecyclerViewItem.Product, number: Int, user: User) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCart(user = user, product = product, number = number)
        }

    fun getItem(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val productDetailFlow = repository.getProductDetail(idProduct = id)
            productDetailFlow.collect{
                chosenProduct.postValue(it)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()
                return ProductDetailInformationViewModel((application as MyApplication).repository) as T
            }
        }
    }
}