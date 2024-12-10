package com.dicoding.skinalyzecapstone.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("RegisterResponse")
	val registerResponse: List<RegisterResponseItem>
)

data class RegisterResponseItem(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("id_user")
	val idUser: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)
