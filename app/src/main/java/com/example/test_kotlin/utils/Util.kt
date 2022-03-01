package com.example.test_kotlin.utils

import android.util.Log
import androidx.fragment.app.Fragment



fun Fragment.LogData(message:String){
    Log.d(this.javaClass.simpleName, "Log -->: "+ message)
}