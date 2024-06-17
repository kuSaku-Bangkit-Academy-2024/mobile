package com.capstone.kusaku.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.capstone.kusaku.data.local.UserSession
import com.capstone.kusaku.data.remote.AuthRepository
import com.capstone.kusaku.data.remote.UserRepository
import com.capstone.kusaku.data.remote.request.LoginRequest
import com.capstone.kusaku.data.remote.response.LoginData
import com.capstone.kusaku.data.remote.response.UserData
import com.capstone.kusaku.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private fun saveUserSession(userData: UserData?, loginData: LoginData) {
        viewModelScope.launch {
            val userSession = UserSession(
                loginData.accessToken,
                userData?.name,
                userData?.email,
                userData?.income.toString(),
                loginData.refreshToken,
            )
            authRepository.saveSession(userSession)
        }
    }

    fun login(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val loginRequest = LoginRequest(email, password)
            val loginResponse = authRepository.login(loginRequest)

            val userResponse =
                userRepository.getUserDetail("Bearer ${loginResponse.data.accessToken}")
            saveUserSession(userResponse.data, loginResponse.data)

            emit(Resource.success(loginResponse))
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error occurred"))
        }
    }
}