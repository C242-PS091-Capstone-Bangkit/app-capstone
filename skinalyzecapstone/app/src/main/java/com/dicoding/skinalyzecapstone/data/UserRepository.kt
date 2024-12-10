package com.dicoding.skinalyzecapstone.data

import com.dicoding.skinalyzecapstone.data.api.ApiService
import com.dicoding.skinalyzecapstone.data.pref.UserPreference

class UserRepository (
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepository(userPreference,apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}