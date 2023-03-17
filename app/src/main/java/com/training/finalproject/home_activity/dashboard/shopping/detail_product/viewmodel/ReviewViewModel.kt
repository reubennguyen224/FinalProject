package com.training.finalproject.home_activity.dashboard.shopping.detail_product.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.finalproject.data.getAPI
import com.training.finalproject.model.ReviewItem
import com.training.finalproject.model.ReviewList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewViewModel : ViewModel() {
    private val reviewList = ReviewList()
    val reviewListLiveData = MutableLiveData<ReviewList>()

    fun getReviewList() {
        viewModelScope.launch(Dispatchers.IO) {
            reviewList.clear()
            getAPI.getReviewsAPI().enqueue(object : Callback<List<ReviewItem>> {
                override fun onResponse(
                    call: Call<List<ReviewItem>>,
                    response: Response<List<ReviewItem>>
                ) {
                    val body = response.body()
                    if (body != null) {
                        reviewList.addAll(body)
                    }
                    reviewListLiveData.postValue(reviewList)
                }

                override fun onFailure(call: Call<List<ReviewItem>>, t: Throwable) = Unit
            })

        }
    }
}