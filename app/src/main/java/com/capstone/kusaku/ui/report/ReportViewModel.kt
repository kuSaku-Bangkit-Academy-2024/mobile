package com.capstone.kusaku.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.capstone.kusaku.data.remote.ExpenseRepository
import com.capstone.kusaku.utils.DateHelper
import com.capstone.kusaku.utils.Resource
import kotlinx.coroutines.Dispatchers

class ReportViewModel(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {
    fun getTotalExpensesByCategory() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val date = DateHelper.getDateInYearAndMonthFormat(-1)
            val response = expenseRepository.getExpensesByCategory(date)
            emit(Resource.success(response))
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error occurred"))
        }
    }

    fun getAdvices() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = expenseRepository.getAdvices()
            emit(Resource.success(response))
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error occurred"))
        }
    }
}