package com.training.finalproject.utils

import android.app.Application
import com.training.finalproject.data.AppRepository

class MyApplication : Application() {

    val repository by lazy { AppRepository(this) }
}