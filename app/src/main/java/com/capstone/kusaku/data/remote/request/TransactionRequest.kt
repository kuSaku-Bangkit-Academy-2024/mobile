package com.capstone.kusaku.data.remote.request

data class TransactionRequest (
    val timestamp: String,
    val describe: String,
    val price: Int,
    val category: String
)