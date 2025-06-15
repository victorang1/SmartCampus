package com.smartcampus.api.model

import com.google.gson.annotations.SerializedName

data class RemoteMahasiswa(
    @SerializedName("nim")
    val nim: String = "",

    @SerializedName("nama")
    val nama: String = "",

    @SerializedName("email")
    val email: String = "",

    @SerializedName("tanggal_lahir")
    val tanggalLahir: String = "",

    @SerializedName("jenis_kelamin")
    val jenisKelamin: String = "",

    @SerializedName("no_telepon")
    val noTelepon: String = "",

    @SerializedName("alamat_domisili")
    val alamatDomisili: String = "",

    @SerializedName("agama")
    val agama: String = "",

    @SerializedName("program_studi")
    val programStudi: String = "",

    @SerializedName("jurusan")
    val jurusan: String = "",

    @SerializedName("angkatan")
    val angkatan: String = "",

    @SerializedName("status_mahasiswa")
    val statusMahasiswa: String = "",

    @SerializedName("ormawa")
    val ormawa: String = "",

    @SerializedName("riwayat_prestasi")
    val riwayatPrestasi: String = "",

    @SerializedName("foto_profile_url")
    val fotoProfileUrl: String = "",

    @SerializedName("kode_kelas")
    val kodeKelas: String = ""
)

data class RemoteDosen(
    @SerializedName("nip")
    val nip: String = "",

    @SerializedName("nama")
    val nama: String = "",

    @SerializedName("tanggal_lahir")
    val tanggalLahir: String = "",

    @SerializedName("jenis_kelamin")
    val jenisKelamin: String = "",

    @SerializedName("no_telepon")
    val noTelepon: String = "",

    @SerializedName("email")
    val email: String = "",

    @SerializedName("alamat_domisili")
    val alamatDomisili: String = "",

    @SerializedName("agama")
    val agama: String = "",

    @SerializedName("program_studi")
    val programStudi: String = "",

    @SerializedName("jurusan")
    val jurusan: String = "",

    @SerializedName("gelar")
    val gelar: String = "",

    @SerializedName("tanggal_bergabung")
    val tanggalBergabung: String = "",

    @SerializedName("jabatan_akademik")
    val jabatanAkademik: String = "",

    @SerializedName("foto_profile_url")
    val fotoProfileUrl: String = ""
)

data class RemoteLoginRequest(
    @SerializedName("username")
    val username: String,

    @SerializedName("password")
    val password: String
)

data class RemoteLoginResponse(
    @SerializedName("role")
    val role: String = "",

    @SerializedName("user")
    val user: Any? = null
) {
    fun getMahasiswa(): RemoteMahasiswa? {
        return if (role == "mahasiswa" && user is Map<*, *>) {
            val gson = com.google.gson.Gson()
            val json = gson.toJson(user)
            gson.fromJson(json, RemoteMahasiswa::class.java)
        } else null
    }

    fun getDosen(): RemoteDosen? {
        return if (role == "dosen" && user is Map<*, *>) {
            val gson = com.google.gson.Gson()
            val json = gson.toJson(user)
            gson.fromJson(json, RemoteDosen::class.java)
        } else null
    }
}

data class RemoteUpdateMahasiswaRequest(
    @SerializedName("nama")
    val nama: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("tanggal_lahir")
    val tanggalLahir: String? = null,

    @SerializedName("jenis_kelamin")
    val jenisKelamin: String? = null,

    @SerializedName("no_telepon")
    val noTelepon: String? = null,

    @SerializedName("alamat_domisili")
    val alamatDomisili: String? = null,

    @SerializedName("agama")
    val agama: String? = null,

    @SerializedName("program_studi")
    val programStudi: String? = null,

    @SerializedName("jurusan")
    val jurusan: String? = null,

    @SerializedName("angkatan")
    val angkatan: String? = null,

    @SerializedName("status_mahasiswa")
    val statusMahasiswa: String? = null,

    @SerializedName("ormawa")
    val ormawa: String? = null,

    @SerializedName("riwayat_prestasi")
    val riwayatPrestasi: String? = null,

    @SerializedName("foto_profile_url")
    val fotoProfileUrl: String? = null,

    @SerializedName("kode_kelas")
    val kodeKelas: String? = null
)