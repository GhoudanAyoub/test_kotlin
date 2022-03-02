package com.example.test_kotlin.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class action(
    var userId: Int?,
    var id: Int?,
    var title: String?,
    var completed: Boolean?
) : Parcelable