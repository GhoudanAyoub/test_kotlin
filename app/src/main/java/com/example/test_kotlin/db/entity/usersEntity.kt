package com.example.test_kotlin.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "USERS")
data class usersEntity(
    @PrimaryKey var id: Int?,
    val name: String?,
    val username: String?,
    val email: String?,
    val phone: String?,
    val website: String?
)