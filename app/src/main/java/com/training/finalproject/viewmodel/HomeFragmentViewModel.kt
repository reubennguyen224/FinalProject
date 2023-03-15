package com.training.finalproject.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.training.finalproject.model.*
import com.training.finalproject.utils.AppRepository
import com.training.finalproject.utils.getAPI
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragmentViewModel() : ViewModel() {

    lateinit var viewModelRepository: AppRepository

    private val productList = ArrayList<HomeRecyclerViewItem.Product>()
    val cartList = ArrayList<CartItem>()
    private var homeList = ArrayList<Any>()
    val homeListLiveData = MutableLiveData<ArrayList<Any>>()
    val userAccountLiveData = MutableLiveData<User>()
    var userAccount = User()
    val numberProduct = MutableLiveData<Int>()
    val cartListLiveData = MutableLiveData<ArrayList<CartItem>>()
    val chosenItemCartList = MutableLiveData<ArrayList<CartItem>?>()
    val chosenProduct = MutableLiveData<HomeRecyclerViewItem.Product?>()

    fun setRepository(repository: AppRepository) {
        viewModelRepository = repository
    }

    fun changeNumber(state: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        if (state == true) {
            var number = numberProduct.value
            if (number != null) {
                numberProduct.postValue(number + 1)
            }
        } else {
            var number = numberProduct.value
            if (number != null && number > 0) {
                numberProduct.postValue(number - 1)
            }
        }
    }


    var bannerList = ArrayList<HomeRecyclerViewItem.Banner>()

    fun resetNumber() = viewModelScope.launch(Dispatchers.IO) {
        numberProduct.postValue(1)
    }

    private fun getAllProducts() {
        val productsList = ArrayList<HomeRecyclerViewItem.Product>()
        productList.clear()
        viewModelScope.launch(Dispatchers.IO) {
            getAPI.getProductsAPI().enqueue(object : Callback<List<ProductX>> {
                override fun onResponse(
                    call: Call<List<ProductX>>,
                    response: Response<List<ProductX>>
                ) {
                    val bodyList = response.body()
                    if (bodyList != null) {
                        for (i in bodyList) {
                            productsList.add(
                                HomeRecyclerViewItem.Product(
                                    id = i.id,
                                    name = i.name,
                                    seller = i.seller,
                                    sale_price = i.sale_price,
                                    star = i.star,
                                    price = i.price,
                                    image = i.image,
                                    description = ""
                                )
                            )
                        }

                        homeList.addAll(productsList)

                        productList.addAll(productsList)
                        homeListLiveData.postValue(homeList)
                    }
                }

                override fun onFailure(call: Call<List<ProductX>>, t: Throwable) = Unit
            })
        }
    }

    private fun getAllBanners() {
        viewModelScope.launch(Dispatchers.IO) {
            getAPI.getBannerAPI().enqueue(object : Callback<List<HomeRecyclerViewItem.Banner>> {
                override fun onResponse(
                    call: Call<List<HomeRecyclerViewItem.Banner>>,
                    response: Response<List<HomeRecyclerViewItem.Banner>>
                ) {
                    val bodyList = response.body()
                    bannerList.clear()
                    if (bodyList != null)
                        bannerList.addAll(bodyList)
                    homeList[0] = bannerList
                    homeListLiveData.postValue(homeList)
                }

                override fun onFailure(
                    call: Call<List<HomeRecyclerViewItem.Banner>>,
                    t: Throwable
                ) = Unit
            })
        }
    }

    fun getHomeList() {
        viewModelScope.launch {
            homeList.clear()
            getAllBanners()
            getAllProducts()
            val title = HomeRecyclerViewItem.Title("New Product")

            val homeItemList = ArrayList<Any>()
            homeItemList.add(bannerList)
            homeItemList.add(title)
            homeItemList.addAll(productList)

            homeList = homeItemList
            delay(1000)
            homeListLiveData.postValue(homeItemList)
        }
    }

//    fun loadMore() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val currentSize = productList.size
//            if (currentSize + 8 <= _productList.size) {
//                for (i in currentSize..currentSize + 7) {
//                    productList.add(_productList[i])
//                }
//            } else {
//                for (i in currentSize.._productList.size)
//                    productList.add(_productList[i])
//            }
//            delay(5000)
//            productListLiveData.postValue(productList)
//            getHomeList()
//        }
//    }

    fun getRememberedAccount(sharedPreferences: SharedPreferences) {
        viewModelScope.launch(Dispatchers.IO) {
            val gson = Gson()
            val userInformation = sharedPreferences.getString("RememberUser", null)
            userInformation?.let {
                val user = gson.fromJson(it, User::class.java)
                userAccount = user
                userAccountLiveData.postValue(user)
            }
        }
    }

    fun getItem(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getAPI.getProductDetail(id).enqueue(object : Callback<HomeRecyclerViewItem.Product> {
                override fun onResponse(
                    call: Call<HomeRecyclerViewItem.Product>,
                    response: Response<HomeRecyclerViewItem.Product>
                ) {
                    val body = response.body()
                    if (body != null)
                        chosenProduct.postValue(body)
                }

                override fun onFailure(call: Call<HomeRecyclerViewItem.Product>, t: Throwable) =
                    Unit
            })
        }
    }

    fun addToCart(id: Int, number: Int, repository: AppRepository) =
        viewModelScope.launch(Dispatchers.IO) {
            val user = userAccount
            val product = productList.find {
                it.id == id
            }
            product?.let {
                repository.insertCart(user = user, product = product, number = number)
            }
        }

    val cartRoomList = ArrayList<Cart>()

    fun getCart(repository: AppRepository) {
        viewModelScope.launch(Dispatchers.IO) {
            cartList.clear()
            val list = repository.getAllCart()
            cartRoomList.clear()
            cartRoomList.addAll(list)
            val itemList = ArrayList<CartItem>()
            for (i in list.indices) {
                val product = productList.find {
                    it.id == list[i].productID
                }
                product?.let {
                    val item = CartItem(
                        id = list[i].id,
                        product = product,
                        number = list[i].number,
                        checked = false
                    )
                    itemList.add(item)
                    cartList.add(item)
                }
            }
            cartListLiveData.postValue(itemList)
        }

    }

    fun saveChoosenItem(list: ArrayList<CartItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            chosenItemCartList.postValue(null)
            chosenItemCartList.postValue(list)
        }
    }

    fun checkoutCart() {
        viewModelScope.launch(Dispatchers.IO){
            val chosenItem = chosenItemCartList.value ?: ArrayList()
            val checkoutCart = ArrayList<Cart>()
            for (i in chosenItem){
                for (j in cartRoomList)
                    if (i.product?.id == j.productID)
                        checkoutCart.add(j)
            }
            viewModelRepository.deleteCart(checkoutCart.toTypedArray())
        }
    }
}