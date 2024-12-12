package com.dicoding.skinalyzecapstone.data

import com.dicoding.skinalyzecapstone.data.api.ApiServiceGeneral
import com.dicoding.skinalyzecapstone.data.pref.UserModel
import com.dicoding.skinalyzecapstone.data.pref.UserPreference
import com.dicoding.skinalyzecapstone.data.response.*
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userPreference: UserPreference,
    private val apiServiceGeneral: ApiServiceGeneral
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
        return apiServiceGeneral.getUsers()
    }

    suspend fun getUserById(id: Int): UserResponse {
        return apiServiceGeneral.getUserById(id)
    }

    suspend fun registerUser(username: String, email: String, password: String): RegisterResponse {
        return apiServiceGeneral.registerUser(username, email, password)
    }

    suspend fun editUser(id: Int, username: String, email: String, password: String): EditUserResponse {
        return apiServiceGeneral.editUser(id, username, email, password)
    }

    suspend fun deleteUser(id: Int): EditUserResponse {
        return apiServiceGeneral.deleteUser(id)
    }

    suspend fun loginUser(email: String, password: String): LoginResponse {
        return apiServiceGeneral.login(email, password)
    }

    // Reminder APIs
    suspend fun createReminder(
        title: String,
        description: String,
        time: String
    ): CreateReminderResponse {
        return apiServiceGeneral.createReminder(title, description, time)
    }
    suspend fun getReminders(): List<ReminderResponse> {
        return apiServiceGeneral.getReminder()
    }

    suspend fun getRemindersByUserId(idUser: Int): List<ReminderResponse> {
        return apiServiceGeneral.getRemindersByUserId(idUser)
    }


    // History APIs
    suspend fun getHistoryById(id: Int): GetHistoryResponse {
        return apiServiceGeneral.getHistoryById(id)
    }

    suspend fun deleteHistory(id: Int): DeleteHistoryResponse {
        return apiServiceGeneral.deleteHistory(id)
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(
            userPreference: UserPreference,
            apiServiceGeneral: ApiServiceGeneral
        ): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepository(userPreference, apiServiceGeneral)
                INSTANCE = instance
                instance
            }
        }
    }
}
