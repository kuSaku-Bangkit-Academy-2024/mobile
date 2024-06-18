package com.capstone.kusaku.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.capstone.kusaku.data.remote.AuthRepository
import com.capstone.kusaku.data.remote.request.RegisterRequest
import com.capstone.kusaku.utils.Resource
import kotlinx.coroutines.Dispatchers

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun register(email: String, password: String, confirmationPassword: String, income: String, name: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val registerRequest = RegisterRequest(email, password, confirmationPassword, income, name)
            val response = authRepository.register(registerRequest)
            emit(Resource.success(response))
        } catch (exception: Exception) {
            var errorMessage = "An error occurred"
            if (exception.message?.contains("409") == true){
                errorMessage = "Email is in use"
            }
            emit(Resource.error(errorMessage))
        }
    }
}