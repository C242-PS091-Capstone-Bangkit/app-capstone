package com.dicoding.skinalyzecapstone.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    private const val BASE_URL_1 = "http://34.101.62.223:5000/" // Base URL untuk keperluan umum
    private const val BASE_URL_2 = "http://34.101.62.223:6000/" // Base URL untuk scan model

    private fun createRetrofit(baseUrl: String): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // Instance Retrofit untuk API umum
    fun getApiServiceGeneral(): ApiServiceGeneral {
        return createRetrofit(BASE_URL_1).create(ApiServiceGeneral::class.java)
    }

    // Instance Retrofit untuk API scan model
    fun getApiServiceScan(): ApiServiceScan {
        return createRetrofit(BASE_URL_2).create(ApiServiceScan::class.java)
    }
}
