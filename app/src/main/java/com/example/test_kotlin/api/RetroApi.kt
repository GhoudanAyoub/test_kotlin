package com.example.test_kotlin.api

import com.example.test_kotlin.Models.response
import retrofit2.Response
import retrofit2.http.GET

interface RetroApi {
    @GET("users")
    suspend fun getUsers(): Response<response>
}