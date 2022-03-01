package com.example.test_kotlin.di

import com.example.test_kotlin.Models.response
import com.example.test_kotlin.api.RetroApi
import retrofit2.Response
import javax.inject.Inject

class retroRepository @Inject constructor( val retroApi:RetroApi) {
    suspend fun getUsers():Response<response>{
        return retroApi.getUsers()
    }
}