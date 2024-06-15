package com.capstone.kusaku.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetExpensesByCategoryResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: String
)

data class ExpenseItem(

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("totalExpense")
	val totalExpense: Int
)

data class Data(

	@field:SerializedName("expenses")
	val expenses: List<ExpenseItem>
)
