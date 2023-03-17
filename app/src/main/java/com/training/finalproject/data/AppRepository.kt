package com.training.finalproject.data

import android.content.Context
import com.training.finalproject.AppDatabase
import com.training.finalproject.home_activity.dashboard.shopping.cart.model.Cart
import com.training.finalproject.model.HomeRecyclerViewItem
import com.training.finalproject.model.User

class AppRepository(context: Context) {


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