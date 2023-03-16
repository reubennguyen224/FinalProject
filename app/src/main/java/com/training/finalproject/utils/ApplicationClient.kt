package com.training.finalproject.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApplicationClient {

    companion object {
        private const val BASE_URL =
            "https://raw.githubusercontent.com/longvuntq/ntqtest/main/data/" //url of web service
        private lateinit var gson: Gson
        private var retrofit: Retrofit? = null
        private val okHttpBuilder =
            OkHttpClient.Builder() // using to process connect to web service using http protocol
                .readTimeout(9000, TimeUnit.MILLISECONDS) // time limit for read request
                .writeTimeout(9000, TimeUnit.MILLISECONDS) // time limit for write request
                .connectTimeout(15000, TimeUnit.MILLISECONDS) // time limit for connect request
                .retryOnConnectionFailure(true)

        fun getInstance(): Retrofit {
            gson = GsonBuilder().setLenient().create()
            if (retrofit == null)
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create(gson)) // convert json from web service to model anh from model to json
                    .build()
            return retrofit!!
        }

    }
}

val getAPI = ApplicationClient.getInstance().create(ApplicationAPI::class.java)