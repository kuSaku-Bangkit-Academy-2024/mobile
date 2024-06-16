package com.capstone.kusaku.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetAdvicesResponse(

	@field:SerializedName("data")
	val data: Advices,

	@field:SerializedName("status")
	val status: String
)

data class Advices(

	@field:SerializedName("advices")
	val advices: List<String>
)
