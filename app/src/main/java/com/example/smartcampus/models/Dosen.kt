package com.example.smartcampus.models

data class Dosen(
    val nip: String = "",
    val nama: String = "",
    val password: String = "",
    val tanggalLahir: String = "",
    val jenisKelamin: String = "",
    val noTelepon: String = "",
    val email: String = "",
    val alamatDomisili: String = "",
    val agama: String = "",
    val programStudi: String = "",
    val jurusan: String = "",
    val gelar: String = "",
    val tanggalBergabung: String = "",
    val jabatanAkademik: String = "",
    val fotoProfileUrl: String? = null
) 