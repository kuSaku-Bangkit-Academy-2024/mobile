package com.capstone.kusaku.data.remote.retrofit

import com.capstone.kusaku.data.remote.request.DescriptionRequest
import com.capstone.kusaku.data.remote.request.LoginRequest
import com.capstone.kusaku.data.remote.request.RefreshTokenRequest
import com.capstone.kusaku.data.remote.request.RegisterRequest
import com.capstone.kusaku.data.remote.request.TransactionRequest
import com.capstone.kusaku.data.remote.response.CategoryResponse
import com.capstone.kusaku.data.remote.response.GetAdvicesResponse
import com.capstone.kusaku.data.remote.response.GetExpensesByCategoryResponse
import com.capstone.kusaku.data.remote.response.GetExpensesHistoryResponse
import com.capstone.kusaku.data.remote.response.LoginResponse
import com.capstone.kusaku.data.remote.response.RefreshTokenResponse
import com.capstone.kusaku.data.remote.response.RegisterResponse
import com.capstone.kusaku.data.remote.response.TransactionResponse
import com.capstone.kusaku.data.remote.response.UserDetailResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

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
    ): CategoryResponse

    @POST("wallets/expenses")
    suspend fun addTransaction(
        @Body transactionRequest: TransactionRequest,
    ): TransactionResponse

    @GET("wallets/expenses")
    suspend fun getExpensesByCategory(
        @Query("date") date: String
    ): GetExpensesByCategoryResponse

    @GET("advices")
    suspend fun getAdvices(
    ): GetAdvicesResponse

    @POST("auth/token")
    suspend fun refreshToken(
        @Body refreshTokenRequest: RefreshTokenRequest
    ): RefreshTokenResponse

    @GET("wallets/expenses/month")
    suspend fun getExpensesHistory(
        @Query("date") date: String
    ): GetExpensesHistoryResponse
}