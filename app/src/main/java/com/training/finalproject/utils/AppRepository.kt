package com.training.finalproject.utils

import android.content.Context
import com.training.finalproject.AppDatabase
import com.training.finalproject.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository(private val context: Context) {

    var appDAO = AppDatabase.getInstance(context = context).roomDAO()
    val user = appDAO.getAll()
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

    suspend fun updateCart(user: User, product: HomeRecyclerViewItem.Product?, number: Int) {
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

    suspend fun getBadge(uid: Int): Int {
        return appDAO.getBadge(uid)
    }

    fun deleteCart(list: Array<Cart>) {
        appDAO.deleteCart(cart = list)
    }

    fun findProduct(id: Int): HomeRecyclerViewItem.Product? {
        var product: HomeRecyclerViewItem.Product? = null
        getAPI.getProductDetail(id = id)?.enqueue(object : Callback<HomeRecyclerViewItem.Product> {
            override fun onResponse(
                call: Call<HomeRecyclerViewItem.Product>,
                response: Response<HomeRecyclerViewItem.Product>
            ) {
                val productDetail = response.body()
                product = productDetail
            }

            override fun onFailure(call: Call<HomeRecyclerViewItem.Product>, t: Throwable) {
                product = null
            }

        })
        return product
    }

    fun getAllReviews(): List<ReviewItem> {
        var reviewList = ReviewList()
        getAPI?.getReviewsAPI()?.enqueue(object : Callback<List<ReviewItem>> {
            override fun onResponse(
                call: Call<List<ReviewItem>>,
                response: Response<List<ReviewItem>>
            ) {
                val bodyList = response.body()
                if (bodyList != null) {
                    reviewList.addAll(bodyList)
                }
            }

            override fun onFailure(call: Call<List<ReviewItem>>, t: Throwable) {
                reviewList = ReviewList()
            }

        })
        return reviewList
    }


}