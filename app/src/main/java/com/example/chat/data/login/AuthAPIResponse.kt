package com.example.chat.data.login

import com.example.chat.data.model.LoggedInUser
import com.squareup.moshi.Json

data class AuthAPIResponse(
    @Json(name = "token")
    val accessToken: String,
    @Json(name = "user")
    val loggedInUser: LoggedInUser?
)
