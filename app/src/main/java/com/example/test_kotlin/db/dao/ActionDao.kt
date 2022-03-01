package com.example.test_kotlin.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.test_kotlin.db.entity.actionEntity

@Dao
interface ActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(actionEntity: actionEntity):Long

    // NOTE - Since we are already using LIVE-DATA no need to use suspend function
    @Query("SELECT * FROM `ACTION` WHERE userId = :userID")
    fun getAllOfflineAction(userID: Int?): LiveData<actionEntity>

    @Delete
    suspend fun delete(actionEntity: actionEntity)

}