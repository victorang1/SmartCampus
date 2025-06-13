package com.example.smartcampus.pages

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartcampus.R
import com.example.smartcampus.utils.SharedPreferencesUtil

class LoginActivity : AppCompatActivity() {
    private lateinit var etNim: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()
        setupClickListeners()

        sharedPreferencesUtil = SharedPreferencesUtil(this)

        if (sharedPreferencesUtil.isUserLoggedIn()) {
            startActivity(Intent(this, HostActivity::class.java))
            finish()
        }
    }

    private fun initializeViews() {
        etNim = findViewById(R.id.et_nim)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            val username = etNim.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(username, password)
        }
    }

    private fun loginUser(username: String, password: String) {

    }
} 