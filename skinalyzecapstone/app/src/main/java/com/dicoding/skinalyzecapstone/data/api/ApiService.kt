package com.dicoding.skinalyzecapstone.data.api

import com.dicoding.skinalyzecapstone.data.response.CreateReminderResponse
import com.dicoding.skinalyzecapstone.data.response.DeleteHistoryResponse
import com.dicoding.skinalyzecapstone.data.response.EditUserResponse
import com.dicoding.skinalyzecapstone.data.response.GetHistoryResponse
import com.dicoding.skinalyzecapstone.data.response.LoginResponse
import com.dicoding.skinalyzecapstone.data.response.RegisterResponse
import com.dicoding.skinalyzecapstone.data.response.UserResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {

    // User APIs
    @GET("users")
    suspend fun getUsers(): List<UserResponse>

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: Int
    ): UserResponse

    @FormUrlEncoded
    @POST("users")
    suspend fun registerUser(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @PUT("users/{id}")
    suspend fun editUser(
        @Path("id") id: Int,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): EditUserResponse

    @DELETE("users/{id}")
    suspend fun deleteUser(
        @Path("id") id: Int
    ): EditUserResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    // Reminder APIs
//    @GET("reminder")
//    suspend fun getReminders(): List<ReminderResponse>

//    @GET("reminder/{id}")
//    suspend fun getReminderById(
//        @Path("id") id: Int
//    ): ReminderResponse

    @FormUrlEncoded
    @POST("reminder")
    suspend fun createReminder(
        @Field("judul_reminder") title: String,
        @Field("deskripsi") description: String,
        @Field("jam_reminder") time: String
    ): CreateReminderResponse

//    @FormUrlEncoded
//    @PUT("reminder/{id}")
//    suspend fun editReminder(
//        @Path("id") id: Int,
//        @Field("judul_reminder") title: String,
//        @Field("deskripsi") description: String,
//        @Field("jam_reminder") time: String
//    ): UpdateReminderResponse
//
//    @DELETE("reminder/{id}")
//    suspend fun deleteReminder(
//        @Path("id") id: Int
//    ): DeleteResponse

    // History APIs
    @GET("history/{id}")
    suspend fun getHistoryById(
        @Path("id") id: Int
    ): GetHistoryResponse

    @DELETE("history/{id}")
    suspend fun deleteHistory(
        @Path("id") id: Int
    ): DeleteHistoryResponse
}
