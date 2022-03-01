package com.example.test_kotlin.di

import androidx.lifecycle.LiveData
import com.example.test_kotlin.Models.action
import com.example.test_kotlin.Models.users
import com.example.test_kotlin.db.AppDatabase
import com.example.test_kotlin.db.entity.actionEntity
import com.example.test_kotlin.db.entity.usersEntity
import com.example.test_kotlin.di.Transformer.convertActionModelToActionEntity
import com.example.test_kotlin.di.Transformer.convertUserModelToUserEntity
import javax.inject.Inject

class DBRepository  @Inject constructor(val appDatabase: AppDatabase){

    suspend fun insertUser(users: users): Long {
        return appDatabase.UsersDao()
            .insert(convertUserModelToUserEntity(users))
    }

    suspend fun delete(users: users) {
        appDatabase.UsersDao().delete(convertUserModelToUserEntity(users))
    }

    fun getAllOfflineUsers(): LiveData<List<usersEntity>> {
        return appDatabase.UsersDao().getAllOfflineUsers()
    }

    suspend fun insertAction(action: action): Long {
        return appDatabase.ActionDao()
            .insert(convertActionModelToActionEntity(action))
    }

    suspend fun deleteAction(action: action) {
        appDatabase.ActionDao().delete(convertActionModelToActionEntity(action))
    }

    fun getAllOfflineAction(userID:Int?): LiveData<actionEntity> {
        return appDatabase.ActionDao().getAllOfflineAction(userID)
    }

}