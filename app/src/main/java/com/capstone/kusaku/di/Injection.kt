package com.capstone.kusaku.di

import android.content.Context
import com.capstone.kusaku.data.local.UserPreference
import com.capstone.kusaku.data.local.dataStore
import com.capstone.kusaku.data.remote.AuthRepository
import com.capstone.kusaku.data.remote.CategoryRepository
import com.capstone.kusaku.data.remote.UserRepository
import com.capstone.kusaku.data.remote.retrofit.ApiConfig
import com.capstone.kusaku.data.remote.retrofit.ApiService

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return AuthRepository.getInstance(pref, apiService)
    }

    fun provideUserRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }

    fun provideApiService(): ApiService {
        return ApiConfig.getApiService()
    }

    fun provideCategoryRepository(): CategoryRepository {
        val apiService = ApiConfig.getApiService()
        return CategoryRepository.getInstance(apiService)
    }
}