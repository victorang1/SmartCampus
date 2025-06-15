package com.smartcampus.api.model

import com.google.gson.annotations.SerializedName

data class RemoteProdiKelas(
    @SerializedName("prodi")
    val prodi: String = "",

    @SerializedName("kelas")
    val kelas: List<String> = listOf()
)

data class RemoteMahasiswaKelas(
    @SerializedName("nim")
    val nim: String = "",

    @SerializedName("nama")
    val nama: String = "",

    @SerializedName("foto_profile_url")
    val fotoProfileUrl: String? = null
)

data class RemoteNilai(
    @SerializedName("nilai")
    val nilai: List<RemoteNilaiDetail> = listOf(),

    @SerializedName("ipk")
    val ipk: Double = 0.0
)

data class RemoteNilaiDetail(
    @SerializedName("nilai_angka")
    val nilaiAngka: Double = 0.0,

    @SerializedName("nilai_huruf")
    val nilaiHuruf: String = "",

    @SerializedName("keterangan")
    val keterangan: String = "",

    @SerializedName("nama_mk")
    val namaMk: String = "",

    @SerializedName("sks")
    val sks: Int = 0
) 