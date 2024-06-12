package com.capstone.kusaku.data.remote.retrofit

import com.capstone.kusaku.data.remote.request.DescriptionRequest
import com.capstone.kusaku.data.remote.request.LoginRequest
import com.capstone.kusaku.data.remote.request.RegisterRequest
import com.capstone.kusaku.data.remote.request.TransactionRequest
import com.capstone.kusaku.data.remote.response.CategoryResponse
import com.capstone.kusaku.data.remote.response.LoginResponse
import com.capstone.kusaku.data.remote.response.RegisterResponse
import com.capstone.kusaku.data.remote.response.TransactionResponse
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

    @POST("users/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @GET("users/profile")
    suspend fun getUserDetail(
        @Header("Authorization") token: String,
    ): UserDetailResponse

    @POST("wallets/expenses/predict-category")
    suspend fun predictCategory(
        @Body descriptionRequest: DescriptionRequest,
        @Header("Authorization") token: String
    ): CategoryResponse

    @POST("wallets/expenses")
    suspend fun addTransaction(
        @Body transactionRequest: TransactionRequest,
        @Header("Authorization") token: String
    ): TransactionResponse
}