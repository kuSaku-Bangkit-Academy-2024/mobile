package com.capstone.kusaku.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.kusaku.data.local.UserSession
import com.capstone.kusaku.data.remote.AuthRepository

class SplashViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun getUserSession(): LiveData<UserSession> {
        return authRepository.getSession().asLiveData()
    }
}