package com.example.chat.network.findEmail


object FindEmailAPI {
    val retrofitService: FindEmailAPIService by lazy {
        retrofit.create(FindEmailAPIService::class.java)
    }
}