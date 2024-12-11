package com.dicoding.skinalyzecapstone.data.response

import com.google.gson.annotations.SerializedName

data class ReminderResponse(

	@field:SerializedName("judul_reminder")
	val judulReminder: String? = null,

	@field:SerializedName("id_user")
	val id: Int? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("jam_reminder")
	val jamReminder: String? = null,

	@field:SerializedName("id_reminder")
	val idReminder: Int? = null
)
