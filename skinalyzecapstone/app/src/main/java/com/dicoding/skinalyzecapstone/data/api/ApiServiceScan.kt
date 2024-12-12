package com.dicoding.skinalyzecapstone.data.api

import com.dicoding.skinalyzecapstone.data.response.PredictResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServiceScan {
    @Multipart
    @POST("predict")
    suspend fun predictImage(
        @Part image: MultipartBody.Part,
        @Part("id_user") idUser: RequestBody
    ): PredictResponse
}