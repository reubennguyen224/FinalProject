package com.training.finalproject.utils

import com.google.gson.Gson
import com.training.finalproject.data.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException

suspend inline fun <T> safeApiCall(
    crossinline responseFunction: suspend () -> T
): Resource<T> {
    return try {
        val bodyFunction = withContext(Dispatchers.IO){
            responseFunction()
        }
        Resource.success(bodyFunction)
    } catch (e: Exception) {
        Resource.error(null, e.message ?: "Something were wrong!")
    }
}