package com.example.test_kotlin.api

import com.example.test_kotlin.Models.action
import com.example.test_kotlin.Models.users
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroApi {

    @GET("users")
    suspend fun getUsers(): Response<List<users>>

    @GET("todos")
    suspend fun getActions(
        @Query("userId") id: Int?,
    ): Response<List<action>>
}