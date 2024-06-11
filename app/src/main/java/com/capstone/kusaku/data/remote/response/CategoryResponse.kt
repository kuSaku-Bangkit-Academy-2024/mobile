package com.capstone.kusaku.data.remote.response

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("category")
    val category: String
)