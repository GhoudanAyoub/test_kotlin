package com.example.test_kotlin.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test_kotlin.R
import com.example.test_kotlin.parentalcontrol.ParentalControlActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        findViewById<FloatingActionButton>(R.id.fab_parental_control).setOnClickListener {
            startActivity(Intent(this, ParentalControlActivity::class.java))
        }
    }
}