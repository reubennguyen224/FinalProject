package com.training.finalproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.training.finalproject.model.User

class HomeActivityViewModel : ViewModel() {
    val user = MutableLiveData<User>()
}