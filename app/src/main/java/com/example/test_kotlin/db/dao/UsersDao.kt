package com.example.test_kotlin.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.test_kotlin.db.entity.usersEntity

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usersEntity: usersEntity): Long

    // NOTE - Since we are already using LIVE-DATA no need to use suspend function
    @Query("SELECT * FROM USERS")
    fun getAllOfflineUsers(): LiveData<List<usersEntity>>

    @Delete
    suspend fun delete(usersEntity: usersEntity)

}