package com.smartcampus.api.model

import com.google.gson.annotations.SerializedName

data class RemoteMataKuliah(
    @SerializedName("kode_mk")
    val kodeMk: String = "",

    @SerializedName("nama_mk")
    val namaMk: String = "",

    @SerializedName("prodi")
    val prodi: String = "",

    @SerializedName("nama_dosen")
    val namaDosen: String = ""
)

data class RemoteMateriKuliah(
    @SerializedName("id_materi")
    val idMateri: Int = 0,

    @SerializedName("id_jadwal")
    val idJadwal: Int = 0,

    @SerializedName("judul_materi")
    val judulMateri: String = "",

    @SerializedName("deskripsi")
    val deskripsi: String = "",

    @SerializedName("tipe_file")
    val tipeFile: String = "",

    @SerializedName("file_url")
    val fileUrl: String = "",

    @SerializedName("tanggal_upload")
    val tanggalUpload: String = "",

    @SerializedName("uploaded_by")
    val uploadedBy: String = "",

    @SerializedName("created_at")
    val createdAt: String = "",

    @SerializedName("updated_at")
    val updatedAt: String = ""
)

data class RemoteUploadTugasRequest(
    @SerializedName("kode_mk")
    val kodeMk: String,

    @SerializedName("nim")
    val nim: String,

    @SerializedName("kode_kelas")
    val kodeKelas: String,

    @SerializedName("nama_mk")
    val namaMk: String,

    @SerializedName("judul_tugas")
    val judulTugas: String,

    @SerializedName("file_tugas")
    val fileTugas: String,

    @SerializedName("status_tugas")
    val statusTugas: String
) 