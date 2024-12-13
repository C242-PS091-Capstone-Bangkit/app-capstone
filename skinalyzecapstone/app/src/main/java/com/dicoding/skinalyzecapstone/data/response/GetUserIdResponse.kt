package com.dicoding.skinalyzecapstone.data.response

import com.google.gson.annotations.SerializedName

data class GetUserIdResponse(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("id_user")
	val idUser: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
