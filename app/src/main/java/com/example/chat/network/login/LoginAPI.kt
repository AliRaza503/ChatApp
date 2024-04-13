package com.example.chat.network.login

object LoginAPI {
    val retrofitService: LoginAPIService by lazy {
        retrofit.create(LoginAPIService::class.java)
    }
}