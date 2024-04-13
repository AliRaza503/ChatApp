package com.example.chat.network.findEmail

import com.example.chat.MainActivity
import com.example.chat.network.login.getUnsafeOkHttpClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

private const val BASE_URL =
    "${MainActivity.BASE_URL}/api/user/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
var client: OkHttpClient = getUnsafeOkHttpClient()
val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(client)
    .baseUrl(BASE_URL)
    .build()

interface FindEmailAPIService {
    @GET("exists/")
    suspend fun findEmail(@Query("email") email: String, @Header("Authorization") authToken: String): FindEmailAPIResponse
}

data class FindEmailAPIResponse(val found: Boolean)
