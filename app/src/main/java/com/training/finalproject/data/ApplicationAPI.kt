package com.training.finalproject.data

import com.training.finalproject.model.HomeRecyclerViewItem
import com.training.finalproject.model.ProductX
import com.training.finalproject.model.ReviewItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApplicationAPI {

    @GET("banners")
    suspend fun getBannerAPI(): Response<List<HomeRecyclerViewItem.Banner>>

    @GET("products")
    suspend fun getProductsAPI(): Response<List<ProductX>>

    @GET("reviews")
    suspend fun getReviewsAPI(): List<ReviewItem>

    @GET("product/{id}")
    suspend fun getProductDetail(@Path(value = "id") id: Int): Response<HomeRecyclerViewItem.Product>
}

