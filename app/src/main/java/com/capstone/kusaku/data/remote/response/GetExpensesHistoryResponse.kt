package com.capstone.kusaku.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetExpensesHistoryResponse(

	@field:SerializedName("data")
	val data: ExpensesHistory,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class ExpensesHistory (

	@field:SerializedName("expenses")
	val expenses: List<ExpenseItem>
)

data class ExpenseItem(

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("expenseId")
	val expenseId: String,

	@field:SerializedName("describe")
	val describe: String,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("timestamp")
	val timestamp: Int
)
