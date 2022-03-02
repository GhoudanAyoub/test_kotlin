package com.example.test_kotlin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.test_kotlin.db.dao.ActionDao
import com.example.test_kotlin.db.dao.UsersDao
import com.example.test_kotlin.db.entity.actionEntity
import com.example.test_kotlin.db.entity.usersEntity

@Database(
    version = 1,
    entities = [usersEntity::class, actionEntity::class],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun UsersDao(): UsersDao
    abstract fun ActionDao(): ActionDao
}