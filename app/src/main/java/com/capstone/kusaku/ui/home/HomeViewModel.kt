package com.capstone.kusaku.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.capstone.kusaku.data.remote.AuthRepository
import com.capstone.kusaku.data.remote.ExpenseRepository
import com.capstone.kusaku.utils.DateHelper
import com.capstone.kusaku.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val expenseRepository: ExpenseRepository,
) : ViewModel() {

    private val _userName = MutableLiveData<String?>()
    val userName: LiveData<String?> get() = _userName

    private val _income = MutableLiveData<String?>()
    val income: LiveData<String?> get() = _income

    init {
        getUserDetail()
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
            val date = DateHelper.getDateInYearAndMonthFormat()
            val response = expenseRepository.getExpensesByCategory(date)
            emit(Resource.success(response))
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error occurred"))
        }
    }

    fun getExpensesHistory() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val date = DateHelper.getDateInYearAndMonthFormat()
            val response = expenseRepository.getExpensesHistory(date)
            emit(Resource.success(response))
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error occurred"))
        }
    }
}