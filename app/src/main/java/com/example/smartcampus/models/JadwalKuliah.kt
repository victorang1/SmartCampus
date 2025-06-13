package com.example.smartcampus.models

data class JadwalKuliah(
    val idJadwal: Int = 0,
    val idMk: Int = 0,
    val nip: Long = 0L,
    val kelas: Int = 0,
    val hari: String = "",
    val jamMulai: String = "",
    val jamSelesai: String = "",
    val ruang: String = "",
    val semester: Int = 0,
    val tahunAkademik: String = "",
    val createdAt: String = "",
    val updatedAt: String = ""
) 