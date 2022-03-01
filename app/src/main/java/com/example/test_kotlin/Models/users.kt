package com.example.test_kotlin.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class users(
    val id: Int?,
    val name: String?,
    val username: String?,
    val email: String?,
    val phone: String?,
    val website: String?
): Parcelable