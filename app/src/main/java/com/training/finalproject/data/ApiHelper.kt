package com.training.finalproject.data

class ApiHelper(private val applicationAPI: ApplicationAPI) {
    suspend fun getBannersAPI() = applicationAPI.getBannerAPI()

    suspend fun getProductsAPI() = applicationAPI.getProductsAPI()

    suspend fun getReviewsAPI() = applicationAPI.getReviewsAPI()

    suspend fun getProductDetail(idProduct: Int) = applicationAPI.getProductDetail(id = idProduct)
}