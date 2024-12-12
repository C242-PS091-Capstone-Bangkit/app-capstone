package com.dicoding.skinalyzecapstone.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PredictResponse(

	@field:SerializedName("skin_condition")
	val skinCondition: String?,

	@field:SerializedName("recommendation")
	val recommendation: List<RecommendationItem>?,

	@field:SerializedName("skin_type")
	val skinType: String?,

	@field:SerializedName("status")
	val status: String?
) : Parcelable


@Parcelize
data class RecommendationItem(

	@field:SerializedName("nama_produk")
	val namaProduk: String?,

	@field:SerializedName("link_produk")
	val linkProduk: String?,

	@field:SerializedName("gambar_produk")
	val gambarProduk: String?,

	@field:SerializedName("saran_kandungan")
	val saranKandungan: String?
) : Parcelable
