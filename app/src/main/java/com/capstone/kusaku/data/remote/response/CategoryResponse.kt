package com.capstone.kusaku.data.remote.response

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("data")
    val data: CategoryData
)

data class CategoryData(
    @SerializedName("describe")
    val describe: String,

    @SerializedName("category")
    val category: String
)