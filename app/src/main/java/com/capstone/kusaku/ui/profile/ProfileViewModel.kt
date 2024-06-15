package com.capstone.kusaku.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.capstone.kusaku.data.remote.AuthRepository
import com.capstone.kusaku.data.remote.UserRepository
import com.capstone.kusaku.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProfileViewModel(
    private val authRepository: AuthRepository,
    ) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun getUserDetail() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = authRepository.getSession().first()
            emit(Resource.success(response))
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error occurred"))
        }
    }
}