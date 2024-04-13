package com.example.chat.network.signup

data class SignupRequestDTO(
    val displayName: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)
