package com.example.smartcampus.api

import com.example.smartcampus.models.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: Map<String, String>
    ): Response<Map<String, String>>

    @GET("users/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<User>

    @GET("jadwal")
    suspend fun getJadwalKuliah(
        @Header("Authorization") token: String,
        @Query("tanggal") tanggal: String? = null
    ): Response<List<Map<String, Any>>>

    @GET("absensi")
    suspend fun getAbsensiHistory(
        @Header("Authorization") token: String,
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null
    ): Response<List<Map<String, Any>>>

    @POST("absensi/record")
    suspend fun recordAbsensi(
        @Header("Authorization") token: String,
        @Body absensiData: Map<String, Any>
    ): Response<Map<String, String>>
} 