package com.training.finalproject.utils

import android.app.Application
import com.training.finalproject.utils.AppRepository

class MyApplication: Application(){

     val repository by lazy { AppRepository(this) }
}