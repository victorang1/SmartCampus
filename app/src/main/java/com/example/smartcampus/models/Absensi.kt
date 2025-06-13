package com.example.smartcampus.models

data class Absensi(
    val idAbsensi: Int = 0,
    val nim: String = "",
    val idJadwal: Int = 0,
    val tanggal: String = "",
    val waktuAbsen: String = "",
    val statusKehadiran: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) 