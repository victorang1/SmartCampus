package com.example.smartcampus.models

data class Mahasiswa(
    val nim: String = "",
    val nama: String = "",
    val email: String = "",
    val password: String = "",
    val tanggalLahir: String = "",
    val jenisKelamin: String = "",
    val noTelepon: String = "",
    val alamatDomisili: String = "",
    val agama: String = "",
    val programStudi: String = "",
    val jurusan: String = "",
    val angkatan: Int = 0,
    val statusMahasiswa: String = "",
    val ormawa: String? = null,
    val riwayatPrestasi: String? = null,
    val fotoProfileUrl: String? = null
) 