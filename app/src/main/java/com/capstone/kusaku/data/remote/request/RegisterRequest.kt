package com.capstone.kusaku.data.remote.request

data class RegisterRequest(
    val email: String,
    val password: String,
    val confirm_password: String,
    val income: String,
    val name: String,
)