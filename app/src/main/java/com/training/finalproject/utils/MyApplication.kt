package com.training.finalproject.utils

import android.app.Application

class MyApplication : Application() {

    val repository by lazy { AppRepository(this) }
}