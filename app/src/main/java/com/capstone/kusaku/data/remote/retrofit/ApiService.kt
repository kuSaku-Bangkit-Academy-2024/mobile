package com.capstone.kusaku.data.remote.retrofit

import com.capstone.kusaku.data.remote.request.LoginRequest
import com.capstone.kusaku.data.remote.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse
}