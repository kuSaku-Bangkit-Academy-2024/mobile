package com.capstone.kusaku.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.capstone.kusaku.data.remote.AuthRepository
import com.capstone.kusaku.data.remote.CategoryRepository
import com.capstone.kusaku.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val _userName = MutableLiveData<String?>()
    val userName: LiveData<String?> get() = _userName

    private val _income = MutableLiveData<String?>()
    val income: LiveData<String?> get() = _income

    init {
        getUserDetail()
    }

    private suspend fun getToken(): String {
        return authRepository.getSession().first().token!!
    }

    private fun getUserDetail() {
        viewModelScope.launch {
            authRepository.getSession().first().let {
                _userName.value = it.username
                _income.value = it.income
            }
        }
    }

    fun getTotalExpensesByCategory() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val token = getToken()
            val date = getCurrentDateInYearMonthFormat()
            val response = categoryRepository.getExpensesByCategory("Bearer $token", date)
            emit(Resource.success(response))
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error occurred"))
        }
    }

    private fun getCurrentDateInYearMonthFormat(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}