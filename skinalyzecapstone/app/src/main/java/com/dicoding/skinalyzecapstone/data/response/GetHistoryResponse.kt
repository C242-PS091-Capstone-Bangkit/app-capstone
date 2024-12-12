package com.dicoding.skinalyzecapstone.data.response

import com.google.gson.annotations.SerializedName

data class GetHistoryResponse(

	@field:SerializedName("nama_produk")
	val namaProduk: String? = null,

	@field:SerializedName("id_user_data")
	val idUserData: Int? = null,

	@field:SerializedName("skin_condition")
	val skinCondition: String? = null,

	@field:SerializedName("saran_kandungan")
	val saranKandungan: String? = null,

	@field:SerializedName("id_user")
	val idUser: Any? = null,

	@field:SerializedName("link_produk")
	val linkProduk: String? = null,

	@field:SerializedName("skin_type")
	val skinType: String? = null,

	@field:SerializedName("gambar_produk")
	val gambarProduk: String? = null
)
