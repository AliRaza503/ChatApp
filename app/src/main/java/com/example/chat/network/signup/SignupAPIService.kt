package com.example.chat.network.signup

import com.example.chat.MainActivity
import com.example.chat.data.login.AuthAPIResponse
import com.example.chat.network.login.getUnsafeOkHttpClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val BASE_URL =
    "${MainActivity.BASE_URL}/api/auth/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val client = getUnsafeOkHttpClient()
val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

interface SignupAPIService {
    @POST("register/")
    suspend fun register(@Body signupRequestDTO: SignupRequestDTO): AuthAPIResponse
}