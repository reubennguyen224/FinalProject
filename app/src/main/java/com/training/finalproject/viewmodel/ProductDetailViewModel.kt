package com.training.finalproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.finalproject.model.ReviewItem
import com.training.finalproject.model.ReviewList
import com.training.finalproject.utils.getAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailViewModel : ViewModel() {
    private val reviewList = ReviewList()
    val productItem = MutableLiveData<ReviewList>()

    fun getReviewList(){
        viewModelScope.launch(Dispatchers.IO){
            reviewList.clear()
            getAPI.getReviewsAPI().enqueue(object : Callback<List<ReviewItem>>{
                override fun onResponse(
                    call: Call<List<ReviewItem>>,
                    response: Response<List<ReviewItem>>
                ) {
                    val body = response.body()
                    if (body != null){
                        reviewList.addAll(body)
                    }
                    productItem.postValue(reviewList)
                }

                override fun onFailure(call: Call<List<ReviewItem>>, t: Throwable) = Unit
            })

        }
    }
}