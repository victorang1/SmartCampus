package com.example.smartcampus.models

data class Ormawa(
    val idOrmawa: Int = 0,
    val namaOrmawa: String = "",
    val email: String = "",
    val jenisOrmawa: String = "",
    val deskripsi: String = "",
    val logoUrl: String? = null,
    val linkKegiatan: String? = null
) 