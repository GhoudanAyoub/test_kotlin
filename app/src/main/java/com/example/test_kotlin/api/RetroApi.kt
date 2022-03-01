package com.example.test_kotlin.api

import com.example.test_kotlin.Models.response
import com.example.test_kotlin.Models.responseAction
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroApi {

    @GET("users")
    suspend fun getUsers(): Response<response>

    @GET("todos")
    suspend fun getActions(
        @Query("userId") id: Int?,
    ): Response<responseAction>
}