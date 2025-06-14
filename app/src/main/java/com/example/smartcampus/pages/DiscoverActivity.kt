package com.example.smartcampus.pages

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.smartcampus.R

class DiscoverActivity : AppCompatActivity() {
    private lateinit var btnStart: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)

        btnStart = findViewById<LinearLayout>(R.id.btn_start)
        btnStart.setOnClickListener {
            val intent = Intent(this@DiscoverActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
} 