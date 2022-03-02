package com.example.test_kotlin.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ACTION")
data class actionEntity(
    @PrimaryKey var id: Int?,
    var userId: Int?,
    var title: String?,
    var completed: Boolean?
)
