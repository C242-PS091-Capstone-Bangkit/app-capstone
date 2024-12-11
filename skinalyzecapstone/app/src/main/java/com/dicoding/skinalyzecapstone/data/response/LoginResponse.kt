package com.dicoding.skinalyzecapstone.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@SerializedName("message")
	val message: String,

	@SerializedName("token")
	val token: String,

	@SerializedName("user")
	val user: User
)

data class User(
	@SerializedName("id_user")
	val id: Int,

	@SerializedName("email")
	val email: String,

	@SerializedName("username")
	val username: String
)
