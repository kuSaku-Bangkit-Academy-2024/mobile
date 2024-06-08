package com.capstone.kusaku.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.capstone.kusaku.data.local.UserSession
import com.capstone.kusaku.data.remote.AuthRepository
import com.capstone.kusaku.data.remote.request.LoginRequest
import com.capstone.kusaku.data.remote.response.LoginData
import com.capstone.kusaku.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private fun saveSession(loginData: LoginData) {
        viewModelScope.launch {
            val userSession = UserSession(loginData.accessToken)
            authRepository.saveSession(userSession)
        }
    }

    fun login(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val loginRequest = LoginRequest(email, password)
            val response = authRepository.login(loginRequest)
            saveSession(response.data)
            emit(Resource.success(response))
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error occurred"))
        }
    }
}