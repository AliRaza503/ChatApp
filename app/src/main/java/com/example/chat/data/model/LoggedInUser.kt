package com.example.chat.data.model

import com.squareup.moshi.Json

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    @Json(name = "displayName")
    val displayName: String,
    @Json(name="email")
    val email: String,
)