package com.example.smartcampus.pages

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smartcampus.ApiClient
import com.example.smartcampus.R
import com.example.smartcampus.utils.SharedPreferencesUtil
import com.smartcampus.api.model.RemoteDosen
import com.smartcampus.api.model.RemoteLoginRequest
import com.smartcampus.api.model.RemoteMahasiswa
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var etNim: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        etNim = findViewById(R.id.et_nim)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progress_bar)
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            val nim = etNim.text.toString()
            val password = etPassword.text.toString()

            if (nim.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "NIM dan password harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(nim, password)
        }
    }

    private fun loginUser(nim: String, password: String) {
        showLoading(true)

        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.login(
                    RemoteLoginRequest(username = nim, password = password)
                )

                if (response.success && response.data != null) {
                    if (response.data.role == "mahasiswa") {
                        SharedPreferencesUtil.saveMahasiswaData(
                            this@LoginActivity,
                            response.data.getMahasiswa() as RemoteMahasiswa
                        )
                    } else {
                        SharedPreferencesUtil.saveDosenData(
                            this@LoginActivity,
                            response.data.getDosen() as RemoteDosen
                        )
                    }

                    startActivity(Intent(this@LoginActivity, HostActivity::class.java))
                    finish()
                } else {
                    showError(response.message)
                }
            } catch (e: Exception) {
                showError("Terjadi kesalahan: ${e.message}")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnLogin.isEnabled = !isLoading
        etNim.isEnabled = !isLoading
        etPassword.isEnabled = !isLoading
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
} 