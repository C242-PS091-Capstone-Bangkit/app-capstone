package com.dicoding.skinalyzecapstone.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.skinalyzecapstone.data.UserRepository
import com.dicoding.skinalyzecapstone.data.pref.UserModel
import com.dicoding.skinalyzecapstone.data.response.LoginResponse
import kotlinx.coroutines.launch
import kotlin.Result

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userRepository.loginUser(email, password)
                _loginResult.value = Result.success(response)
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveUserSession(user)
        }
    }
}
