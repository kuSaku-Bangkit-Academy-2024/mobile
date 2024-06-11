package com.capstone.kusaku.data.remote.response

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String
)