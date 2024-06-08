package com.capstone.kusaku.data.remote

import com.capstone.kusaku.data.remote.response.UserDetailResponse
import com.capstone.kusaku.data.remote.retrofit.ApiService

class UserRepository (
    private val apiService: ApiService
){
    suspend fun getUserDetail(token: String): UserDetailResponse {
        return apiService.getUserDetail(token)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}