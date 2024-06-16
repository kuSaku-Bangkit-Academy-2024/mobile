package com.capstone.kusaku.data.remote.response

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(

	@field:SerializedName("data")
	val data: AccessToken,

	@field:SerializedName("status")
	val status: String
)

data class AccessToken (

	@field:SerializedName("accessToken")
	val accessToken: String
)
