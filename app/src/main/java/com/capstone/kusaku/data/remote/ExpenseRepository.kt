package com.capstone.kusaku.data.remote

import com.capstone.kusaku.data.remote.request.DescriptionRequest
import com.capstone.kusaku.data.remote.response.CategoryResponse
import com.capstone.kusaku.data.remote.response.GetAdvicesResponse
import com.capstone.kusaku.data.remote.response.GetExpensesByCategoryResponse
import com.capstone.kusaku.data.remote.response.GetExpensesHistoryResponse
import com.capstone.kusaku.data.remote.retrofit.ApiService

class ExpenseRepository(private val apiService: ApiService) {
    suspend fun predictCategory(description: String): CategoryResponse {
        val descriptionRequest = DescriptionRequest(description)
        return apiService.predictCategory(descriptionRequest)
    }

    suspend fun getExpensesByCategory(date: String): GetExpensesByCategoryResponse {
        return apiService.getExpensesByCategory(date)
    }

    suspend fun getAdvices(): GetAdvicesResponse {
        return apiService.getAdvices()
    }

    suspend fun getExpensesHistory(date: String): GetExpensesHistoryResponse {
        return apiService.getExpensesHistory(date)
    }

    companion object {
        @Volatile
        private var instance: ExpenseRepository? = null

        fun getInstance(apiService: ApiService): ExpenseRepository =
            instance ?: synchronized(this) {
                instance ?: ExpenseRepository(apiService)
            }.also { instance = it }
    }
}