package com.example.chat.network.signup

object SignupAPI {
    val retrofitService: SignupAPIService by lazy {
        retrofit.create(SignupAPIService::class.java)
    }
}