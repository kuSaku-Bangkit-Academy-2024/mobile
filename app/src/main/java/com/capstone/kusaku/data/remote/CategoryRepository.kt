package com.capstone.kusaku.data.remote

import com.capstone.kusaku.data.remote.request.DescriptionRequest
import com.capstone.kusaku.data.remote.response.CategoryResponse
import com.capstone.kusaku.data.remote.retrofit.ApiService

class CategoryRepository(private val apiService: ApiService) {
    suspend fun predictCategory(description: String, token: String): CategoryResponse {
        val descriptionRequest = DescriptionRequest(description)
        return apiService.predictCategory(descriptionRequest, token)
    }

    companion object {
        @Volatile
        private var instance: CategoryRepository? = null

        fun getInstance(apiService: ApiService): CategoryRepository =
            instance ?: synchronized(this) {
                instance ?: CategoryRepository(apiService)
            }.also { instance = it }
    }
}