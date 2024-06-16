package com.capstone.kusaku.di

import android.content.Context
import com.capstone.kusaku.data.local.UserPreference
import com.capstone.kusaku.data.local.dataStore
import com.capstone.kusaku.data.remote.AuthRepository
import com.capstone.kusaku.data.remote.ExpenseRepository
import com.capstone.kusaku.data.remote.UserRepository
import com.capstone.kusaku.data.remote.retrofit.ApiConfig
import com.capstone.kusaku.data.remote.retrofit.ApiService

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        return AuthRepository.getInstance(pref, apiService)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        return UserRepository.getInstance(apiService)
    }

    fun provideApiService(context: Context): ApiService {
        val pref = UserPreference.getInstance(context.dataStore)
        return ApiConfig.getApiService(pref)
    }

    fun provideExpenseRepository(context: Context): ExpenseRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        return ExpenseRepository.getInstance(apiService)
    }
}