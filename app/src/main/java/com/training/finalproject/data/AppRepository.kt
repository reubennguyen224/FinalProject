package com.training.finalproject.data

import android.content.Context
import com.training.finalproject.AppDatabase
import com.training.finalproject.home_activity.dashboard.shopping.cart.model.Cart
import com.training.finalproject.model.HomeRecyclerViewItem
import com.training.finalproject.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class AppRepository(context: Context) {


    var appDAO = AppDatabase.getInstance(context = context).roomDAO()
    val user = appDAO.getAll()
    private val apiHelper = ApiHelper(getAPI)
    val productStateFlow = MutableStateFlow<ArrayList<HomeRecyclerViewItem.Product>>(ArrayList())
    val productFailedStateFlow = MutableStateFlow(false)

    suspend fun getReview() = apiHelper.getReviewsAPI()

    suspend fun getBanner(): ArrayList<HomeRecyclerViewItem.Banner> {
        val bannerList = ArrayList<HomeRecyclerViewItem.Banner>()
        try {
            val response = apiHelper.getBannersAPI()
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    bannerList.addAll(data)
                }
            }
        } catch (_: Exception) {
        }
        return bannerList
    }

    suspend fun getProductDetail(idProduct: Int) = flow {
        val response = apiHelper.getProductDetail(idProduct = idProduct)
        try {
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    emit(data)
                }
            }
        } catch (_: Exception) {
        }

    }

    suspend fun getProduct(): ArrayList<HomeRecyclerViewItem.Product> {
        val productsList = ArrayList<HomeRecyclerViewItem.Product>()
        try {
            val response = apiHelper.getProductsAPI()
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    for (i in data) {
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
                }
            } else {
                productFailedStateFlow.emit(true)
            }
        } catch (e: Exception) {
            productFailedStateFlow.emit(true)
        }
        return productsList
    }

    suspend fun insertCart(user: User, product: HomeRecyclerViewItem.Product?, number: Int = 1) {
        val checkExist = product?.let { appDAO.checkCartExist(it.id) }
        if (checkExist?.size == 1)
            appDAO.updateCart(
                Cart(
                    id = checkExist[0].id,
                    uid = checkExist[0].uid,
                    productID = checkExist[0].productID,
                    number = number + checkExist[0].number
                )
            )
        else {
            val cartItem = product?.id?.let { Cart(uid = user.id, productID = it, number = number) }
            if (cartItem != null) {
                appDAO.insertUserCart(cartItem)
            }
        }
    }

    suspend fun updateCart(product: HomeRecyclerViewItem.Product?, number: Int) {
        val checkExist = product?.let { appDAO.checkCartExist(it.id) }
        if (checkExist?.size == 1) {
            appDAO.updateCart(
                Cart(
                    id = checkExist[0].id,
                    uid = checkExist[0].uid,
                    productID = checkExist[0].productID,
                    number = number
                )
            )

        }
    }

    fun getAllCart(): List<Cart> {
        return appDAO.getProduct()
    }

    suspend fun addUser(user: User) {
        appDAO.insertUser(user)
    }

    fun deleteCart(list: Array<Cart>) {
        appDAO.deleteCart(cart = list)
    }


}