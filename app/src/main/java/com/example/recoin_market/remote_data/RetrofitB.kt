package com.example.recoin_market.remote_data

import com.example.recoin_market.ConstantApi
import com.example.recoin_market.repositores.Char_Apiservice
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object RetrofitB {

    val logging = HttpLoggingInterceptor()
    val okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(30,TimeUnit.SECONDS)
        .writeTimeout(30,TimeUnit.SECONDS)
        .addInterceptor(logging)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit :Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .baseUrl(ConstantApi.BASE_URL)
        .build()

    val retrofitService: Char_Apiservice by lazy {
        retrofit.create(Char_Apiservice::class.java)
    }

}