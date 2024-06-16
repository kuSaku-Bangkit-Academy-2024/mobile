package com.capstone.kusaku.data.local

data class UserSession(
    val token: String?,
    val username: String?,
    val email: String?,
    val income: String?,
    val refreshToken: String?
)