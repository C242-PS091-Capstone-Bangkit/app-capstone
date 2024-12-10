package com.dicoding.skinalyzecapstone.data.pref
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    // Save user session
    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[KEY_ID_USER] = user.idUser
            preferences[KEY_NAME] = user.name
            preferences[KEY_EMAIL] = user.email
            preferences[KEY_TOKEN] = user.token
            preferences[KEY_IS_LOGIN] = user.isLogin
        }
    }

    // Get user session
    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                idUser = preferences[KEY_ID_USER] ?: "",
                name = preferences[KEY_NAME] ?: "",
                email = preferences[KEY_EMAIL] ?: "",
                token = preferences[KEY_TOKEN] ?: "",
                isLogin = preferences[KEY_IS_LOGIN] ?: false
            )
        }
    }

    // Clear user session
    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val KEY_ID_USER = stringPreferencesKey("id_user")
        private val KEY_NAME = stringPreferencesKey("name")
        private val KEY_EMAIL = stringPreferencesKey("email")
        private val KEY_TOKEN = stringPreferencesKey("token")
        private val KEY_IS_LOGIN = booleanPreferencesKey("is_login")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}

