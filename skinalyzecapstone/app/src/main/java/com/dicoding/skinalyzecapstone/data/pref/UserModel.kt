package com.dicoding.skinalyzecapstone.data.pref

data class UserModel(
    val idUser: String,
    val name: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)