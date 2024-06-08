package com.capstone.kusaku.data.remote.retrofit

import com.capstone.kusaku.data.remote.request.LoginRequest
import com.capstone.kusaku.data.remote.response.LoginResponse
import com.capstone.kusaku.data.remote.response.UserDetailResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @GET("users/profile")
    suspend fun getUserDetail(
        @Header("Authorization") token: String,
    ): UserDetailResponse
}