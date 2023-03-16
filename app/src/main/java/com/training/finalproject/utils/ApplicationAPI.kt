package com.training.finalproject.utils

import com.training.finalproject.model.HomeRecyclerViewItem
import com.training.finalproject.model.ProductX
import com.training.finalproject.model.ReviewItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApplicationAPI {

    @GET("banners")
    fun getBannerAPI(): Call<List<HomeRecyclerViewItem.Banner>>

    @GET("products")
    fun getProductsAPI(): Call<List<ProductX>>

    @GET("reviews")
    fun getReviewsAPI(): Call<List<ReviewItem>>

    @GET("product/{id}")
    fun getProductDetail(@Path(value = "id") id: Int): Call<HomeRecyclerViewItem.Product>
}

