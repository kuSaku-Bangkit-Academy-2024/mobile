package com.capstone.kusaku.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.capstone.kusaku.data.remote.AuthRepository
import com.capstone.kusaku.data.remote.ExpenseRepository
import com.capstone.kusaku.data.remote.request.TransactionRequest
import com.capstone.kusaku.data.remote.response.TransactionResponse
import com.capstone.kusaku.data.remote.retrofit.ApiService
import com.capstone.kusaku.utils.Resource
import com.capstone.kusaku.utils.Status
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionViewModel(
    private val apiService: ApiService,
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _transactionResponse = MutableLiveData<Resource<TransactionResponse>>()
    val transactionResponse: LiveData<Resource<TransactionResponse>> get() = _transactionResponse

    private val _category = MutableLiveData<String>()
    val category: LiveData<String> get() = _category

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun predictCategory(description: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = expenseRepository.predictCategory(description)

            if (response.status == "success") {
                val category = response.data.category
                _category.postValue(category)
                emit(Resource.success(response))
            } else {
                emit(Resource.error("Prediction failed: ${response.status}"))
            }
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error occurred"))
        }
    }

    fun addTransaction(timestamp: String, describe: String, price: Int, category: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val transactionRequest = TransactionRequest(timestamp, describe, price, category)
            val response = apiService.addTransaction(transactionRequest)
            emit(Resource.success(response))
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error occurred"))
        }
    }

    fun convertDateFormat(inputDate: String, inputFormat: String, outputFormat: String): String {
        val inputFormatter = SimpleDateFormat(inputFormat, Locale.getDefault())
        val outputFormatter = SimpleDateFormat(outputFormat, Locale.getDefault())
        val date: Date? = inputFormatter.parse(inputDate)
        return date?.let { outputFormatter.format(it) } ?: ""
    }

    fun handleTransactionResponse(response: Resource<TransactionResponse>): String {
        return when (response.status) {
            Status.SUCCESS -> {
                val data = response.data
                if (data?.status == "success") {
                    data.message
                } else {
                    "Failed to add expense"
                }
            }
            Status.ERROR -> response.message ?: "Error occurred"
            Status.LOADING -> "Loading..."
        }
    }
}