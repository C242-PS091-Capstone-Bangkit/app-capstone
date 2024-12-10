package com.dicoding.skinalyzecapstone.data.response

import com.google.gson.annotations.SerializedName

data class CreateReminderResponse(

	@field:SerializedName("message")
	val message: String
)
