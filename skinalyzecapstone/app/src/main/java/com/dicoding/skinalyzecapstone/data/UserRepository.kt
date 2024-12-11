package com.dicoding.skinalyzecapstone.data

import com.dicoding.skinalyzecapstone.data.api.ApiService
import com.dicoding.skinalyzecapstone.data.pref.UserModel
import com.dicoding.skinalyzecapstone.data.pref.UserPreference
import com.dicoding.skinalyzecapstone.data.response.*
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    // User Preferences
    suspend fun saveUserSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun getUserSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun clearUserSession() {
        userPreference.clearSession()
    }

    // User APIs
    suspend fun getUsers(): List<UserResponse> {
        return apiService.getUsers()
    }

    suspend fun getUserById(id: Int): UserResponse {
        return apiService.getUserById(id)
    }

    suspend fun registerUser(username: String, email: String, password: String): RegisterResponse {
        return apiService.registerUser(username, email, password)
    }

    suspend fun editUser(id: Int, username: String, email: String, password: String): EditUserResponse {
        return apiService.editUser(id, username, email, password)
    }

    suspend fun deleteUser(id: Int): EditUserResponse {
        return apiService.deleteUser(id)
    }

    suspend fun loginUser(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    // Reminder APIs
    suspend fun createReminder(
        title: String,
        description: String,
        time: String
    ): CreateReminderResponse {
        return apiService.createReminder(title, description, time)
    }
    suspend fun getReminders(): List<ReminderResponse> {
        return apiService.getReminder()
    }

    suspend fun getRemindersByUserId(idUser: Int): List<ReminderResponse> {
        return apiService.getRemindersByUserId(idUser)
    }


    // History APIs
    suspend fun getHistoryById(id: Int): GetHistoryResponse {
        return apiService.getHistoryById(id)
    }

    suspend fun deleteHistory(id: Int): DeleteHistoryResponse {
        return apiService.deleteHistory(id)
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepository(userPreference, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}
