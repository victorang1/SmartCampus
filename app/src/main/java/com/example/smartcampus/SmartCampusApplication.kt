package com.example.smartcampus

import android.app.Application
import android.util.Log
import java.sql.Driver
import java.sql.DriverManager
import java.util.Properties

class SmartCampusApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeMySQLDriver()
    }

    private fun initializeMySQLDriver() {
        try {
            val classLoader = Thread.currentThread().contextClassLoader
            if (classLoader != null) {
                val mysqlDriver = Class.forName("com.mysql.cj.jdbc.Driver").newInstance() as Driver
                DriverManager.registerDriver(mysqlDriver)
                Log.d("SmartCampusApp", "MySQL Driver registered successfully")
            }
        } catch (e: Exception) {
            Log.e("SmartCampusApp", "Error registering MySQL driver", e)
        }
    }
} 