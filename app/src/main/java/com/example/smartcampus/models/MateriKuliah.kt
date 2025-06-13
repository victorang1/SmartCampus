package com.example.smartcampus.models

data class MateriKuliah(
    val idMateri: Int = 0,
    val idJadwal: Int = 0,
    val judulMateri: String = "",
    val deskripsi: String = "",
    val tipeFile: String = "",
    val fileUrl: String = "",
    val tanggalUpload: String = "",
    val uploadedBy: String = "",
    val createdAt: String = "",
    val updatedAt: String = ""
) 