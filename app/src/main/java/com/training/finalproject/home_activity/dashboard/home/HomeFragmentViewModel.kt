package com.training.finalproject.home_activity.dashboard.home

import android.content.SharedPreferences
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.gson.Gson
import com.training.finalproject.data.AppRepository
import com.training.finalproject.data.getAPI
import com.training.finalproject.home_activity.dashboard.shopping.cart.model.Cart
import com.training.finalproject.home_activity.dashboard.shopping.cart.model.CartItem
import com.training.finalproject.model.HomeRecyclerViewItem
import com.training.finalproject.model.ProductX
import com.training.finalproject.model.User
import com.training.finalproject.utils.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragmentViewModel(private val repository: AppRepository) : ViewModel() {

    val productList = ArrayList<HomeRecyclerViewItem.Product>()
    val cartList = ArrayList<CartItem>()
    private var homeList = ArrayList<Any>()
    val homeListLiveData = MutableLiveData<ArrayList<Any>>()
    val userAccountLiveData = MutableLiveData<User>()
    var userAccount = User()

    val cartListLiveData = MutableLiveData<ArrayList<CartItem>>()

    var bannerList = ArrayList<HomeRecyclerViewItem.Banner>()

    init {
        getHomeList()
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

                        //homeList.addAll(productsList)

                        productList.addAll(productsList)
                        getHomeList()
                    }
                }

                override fun onFailure(call: Call<List<ProductX>>, t: Throwable) = Unit
            })
        }
    }

    fun setCartValue(list: ArrayList<CartItem>?) {
        if (list != null){
            cartList.clear()
            cartList.addAll(list)
        }
        cartListLiveData.postValue(cartList)
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
                    if (bodyList != null) {
                        bannerList.addAll(bodyList)

                    }
                    getHomeList()
                }

                override fun onFailure(
                    call: Call<List<HomeRecyclerViewItem.Banner>>,
                    t: Throwable
                ) = Unit
            })
        }
    }

    fun getData() {
        getAllBanners()
        getAllProducts()
    }

    fun getHomeList() {

        viewModelScope.launch(Dispatchers.Main) {
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

    private fun getMoreProduct(lastProductId: Int): ArrayList<HomeRecyclerViewItem.Product> {
        val result = ArrayList<HomeRecyclerViewItem.Product>()
        viewModelScope.launch(Dispatchers.IO) {

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


    private val cartRoomList = ArrayList<Cart>()

    fun getCart() {
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
            delay(5000)
            cartListLiveData.postValue(itemList)
        }

    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()
                return HomeFragmentViewModel((application as MyApplication).repository) as T
            }
        }
    }
}