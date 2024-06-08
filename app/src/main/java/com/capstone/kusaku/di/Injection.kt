package com.capstone.kusaku.di

import android.content.Context
import com.capstone.kusaku.data.local.UserPreference
import com.capstone.kusaku.data.local.dataStore
import com.capstone.kusaku.data.remote.AuthRepository
import com.capstone.kusaku.data.remote.retrofit.ApiConfig

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return AuthRepository.getInstance(pref, apiService)
    }
}