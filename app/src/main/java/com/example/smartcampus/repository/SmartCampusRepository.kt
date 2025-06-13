package com.example.smartcampus.repository

import com.example.smartcampus.api.RetrofitClient
import com.example.smartcampus.models.User
import retrofit2.Response

class SmartCampusRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun login(username: String, password: String): Response<Map<String, String>> {
        return apiService.login(mapOf(
            "username" to username,
            "password" to password
        ))
    }

    suspend fun getUserProfile(token: String): Response<User> {
        return apiService.getUserProfile("Bearer $token")
    }

    suspend fun getJadwalKuliah(token: String, tanggal: String? = null): Response<List<Map<String, Any>>> {
        return apiService.getJadwalKuliah("Bearer $token", tanggal)
    }

    suspend fun getAbsensiHistory(
        token: String,
        startDate: String? = null,
        endDate: String? = null
    ): Response<List<Map<String, Any>>> {
        return apiService.getAbsensiHistory("Bearer $token", startDate, endDate)
    }

    suspend fun recordAbsensi(token: String, jadwalId: Int, latitude: Double, longitude: Double): Response<Map<String, String>> {
        return apiService.recordAbsensi(
            "Bearer $token",
            mapOf(
                "jadwal_id" to jadwalId,
                "latitude" to latitude,
                "longitude" to longitude,
                "timestamp" to System.currentTimeMillis()
            )
        )
    }
} 