package com.capstone.kusaku.data.remote

import com.capstone.kusaku.data.local.UserSession
import com.capstone.kusaku.data.local.UserPreference
import com.capstone.kusaku.data.remote.request.LoginRequest
import com.capstone.kusaku.data.remote.request.RegisterRequest
import com.capstone.kusaku.data.remote.response.LoginResponse
import com.capstone.kusaku.data.remote.response.RegisterResponse
import com.capstone.kusaku.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class AuthRepository (
    private val userPreference: UserPreference,
    private val apiService: ApiService
){
    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return apiService.login(loginRequest)
    }

    suspend fun register(registerRequest: RegisterRequest): RegisterResponse {
        return apiService.register(registerRequest)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getSession(): Flow<UserSession> {
        return userPreference.getSession()
    }

    suspend fun saveSession(user: UserSession) {
        userPreference.saveSession(user)
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(userPreference, apiService)
            }.also { instance = it }
    }
}