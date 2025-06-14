package com.smartcampus.api.model

import com.google.gson.annotations.SerializedName

data class RemoteJadwal(
    @SerializedName("jam_mulai")
    val jamMulai: String = "",

    @SerializedName("jam_selesai")
    val jamSelesai: String = "",

    @SerializedName("kode_mk")
    val kodeMk: String = "",

    @SerializedName("nama_mk")
    val namaMk: String = "",

    @SerializedName("nama_dosen")
    val namaDosen: String = "",

    @SerializedName("ruang")
    val ruang: String = ""
)

data class RemoteAbsenDetail(
    @SerializedName("date")
    val date: String = "",

    @SerializedName("jam_mulai")
    val jamMulai: String = "",

    @SerializedName("jam_selesai")
    val jamSelesai: String = "",

    @SerializedName("kode_mk")
    val kodeMk: String = "",

    @SerializedName("nama_mk")
    val namaMk: String = "",

    @SerializedName("nama_dosen")
    val namaDosen: String = "",

    @SerializedName("ruang")
    val ruang: String = "",

    @SerializedName("id_jadwal")
    val idJadwal: Int = 0
)

data class RemoteAbsenRequest(
    @SerializedName("nim")
    val nim: String,

    @SerializedName("id_jadwal")
    val idJadwal: Int,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("status_kehadiran")
    val statusKehadiran: String
)

data class RemoteAbsenCheckResponse(
    @SerializedName("has_attended")
    val hasAttended: Boolean = false,

    @SerializedName("attendance")
    val attendance: RemoteAbsenData? = null
)

data class RemoteAbsenData(
    @SerializedName("nim")
    val nim: String = "",

    @SerializedName("id_jadwal")
    val idJadwal: Int = 0,

    @SerializedName("tanggal")
    val tanggal: String = "",

    @SerializedName("waktu_absen")
    val waktuAbsen: String = "",

    @SerializedName("latitude")
    val latitude: Double = 0.0,

    @SerializedName("longitude")
    val longitude: Double = 0.0,

    @SerializedName("status_kehadiran")
    val statusKehadiran: String = "",

    @SerializedName("created_at")
    val createdAt: String = "",

    @SerializedName("updated_at")
    val updatedAt: String = ""
)