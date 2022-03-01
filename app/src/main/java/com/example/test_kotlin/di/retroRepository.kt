package com.example.test_kotlin.di

import com.example.test_kotlin.Models.action
import com.example.test_kotlin.Models.users
import com.example.test_kotlin.api.RetroApi
import retrofit2.Response
import javax.inject.Inject

class retroRepository @Inject constructor( val retroApi:RetroApi) {

    suspend fun getUsers():Response<List<users>>{
        return retroApi.getUsers()
    }

    suspend fun getAction(id:Int?):Response<List<action>>{
        return retroApi.getActions(id)
    }
}