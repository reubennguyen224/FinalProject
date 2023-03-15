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
//            homeList.clear()
            val deferredBanner = async(Dispatchers.IO + SupervisorJob()) {
                getAllBanners()
            }
            deferredBanner.await()
            deferredBanner.getCompleted()
            val deferredProduct = async(Dispatchers.IO + SupervisorJob()) {
                getAllProducts()
            }
            deferredProduct.await()
            val title = HomeRecyclerViewItem.Title("New Product")

            val homeItemList = ArrayList<Any>()
            homeItemList.add(bannerList)
            homeItemList.add(title)
            homeItemList.addAll(productList)

            homeList = homeItemList
            homeListLiveData.postValue(homeList)
        }
    }
    private val statusLoadMoreLiveData = MutableLiveData<String>()

    fun loadMore(lastProductId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            var statusLoadMore = ""
            val loadMoreProduct = getMoreProduct(lastProductId = lastProductId)
            if (loadMoreProduct.size < 8) statusLoadMore = "OUT_OF_STOCK"
            else if (loadMoreProduct.size == 8) statusLoadMore = "CAN_LOAD_MORE"
            statusLoadMoreLiveData.postValue(statusLoadMore)
            delay(5000)
            homeList.addAll(loadMoreProduct)
        }
    }

    private fun getMoreProduct(lastProductId: Int): ArrayList<HomeRecyclerViewItem.Product>{
        val result = ArrayList<HomeRecyclerViewItem.Product>()
        viewModelScope.launch(Dispatchers.IO){

        }
        return result
    }

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

    private val cartRoomList = ArrayList<Cart>()

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

    fun saveChosenItem(list: ArrayList<CartItem>) {
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

    fun deleteItem(position: Int){
        viewModelScope.launch(Dispatchers.IO){
            val item  = cartList[position]
            val delete = ArrayList<Cart>()
            for (i in cartRoomList)
                if (item.product?.id == i.productID){
                    delete.add(i)
                }
            viewModelRepository.deleteCart(delete.toTypedArray())
            cartList.remove(item)
            cartListLiveData.postValue(cartList)
        }
    }

    fun updateItemCart(position: Int, isAdd: Boolean){
        viewModelScope.launch(Dispatchers.IO){
            val product = cartList[position].product
            var number = cartList[position].number
            if(isAdd){
                number += 1
            } else{
                number -=1
            }
            viewModelRepository.updateCart(userAccount, product, number)
            cartList[position].number = number
            cartListLiveData.postValue(cartList)
        }
    }
}