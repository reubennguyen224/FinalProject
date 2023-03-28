package com.training.finalproject.home_activity.dashboard.shopping.detail_product.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.training.finalproject.data.AppRepository
import com.training.finalproject.data.Resource
import com.training.finalproject.data.getAPI
import com.training.finalproject.home_activity.dashboard.home.HomeFragmentViewModel
import com.training.finalproject.model.ReviewItem
import com.training.finalproject.model.ReviewList
import com.training.finalproject.utils.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewViewModel (private val reviewRepository: AppRepository): ViewModel() {
    private val reviewList = ReviewList()
    val reviewListLiveData = MutableLiveData<ReviewList>()

    fun getReviewList()  = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = reviewRepository.getReview()))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message ?: "Failed"))
        }
    }

    companion object{
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return ReviewViewModel((application as MyApplication).repository) as T
            }
        }
    }
}