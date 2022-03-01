package com.example.test_kotlin.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class response (
    var Users:List<users>
):Parcelable