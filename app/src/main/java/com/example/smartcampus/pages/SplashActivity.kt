package com.example.smartcampus.pages

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.smartcampus.R
import com.example.smartcampus.utils.SharedPreferencesUtil

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            if (SharedPreferencesUtil.isUserLoggedIn(this)) {
                startActivity(Intent(this, HostActivity::class.java))
                finish()
            } else {
                val intent = Intent(this@SplashActivity, DiscoverActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 1500)
    }
}