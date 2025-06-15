package com.example.smartcampus

import com.smartcampus.api.model.RemoteAbsenCheckResponse
import com.smartcampus.api.model.RemoteAbsenDetail
import com.smartcampus.api.model.RemoteAbsenRequest
import com.smartcampus.api.model.RemoteJadwal
import com.smartcampus.api.model.RemoteLoginRequest
import com.smartcampus.api.model.RemoteLoginResponse
import com.smartcampus.api.model.RemoteMahasiswa
import com.smartcampus.api.model.RemoteMahasiswaKelas
import com.smartcampus.api.model.RemoteMataKuliah
import com.smartcampus.api.model.RemoteMateriKuliah
import com.smartcampus.api.model.RemoteNilai
import com.smartcampus.api.model.RemoteProdiKelas
import com.smartcampus.api.model.RemoteResponse
import com.smartcampus.api.model.RemoteUpdateMahasiswaRequest
import com.smartcampus.api.model.RemoteUploadTugasRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    suspend fun login(
        @Body request: RemoteLoginRequest
    ): RemoteResponse<RemoteLoginResponse>

    @GET("jadwal/by-day/{nim}/{hari}")
    suspend fun getJadwalByDay(
        @Path("nim") nim: String,
        @Path("hari") hari: String
    ): RemoteResponse<List<RemoteJadwal>>

    @GET("jadwal/current/{nim}")
    suspend fun getCurrentJadwal(
        @Path("nim") nim: String
    ): RemoteResponse<RemoteAbsenDetail>

    @POST("absensi")
    suspend fun submitAbsensi(
        @Body request: RemoteAbsenRequest
    ): RemoteResponse<Any>

    @GET("absensi/check/{nim}/{id_jadwal}")
    suspend fun checkAbsensi(
        @Path("nim") nim: String,
        @Path("id_jadwal") idJadwal: Int
    ): RemoteResponse<RemoteAbsenCheckResponse>

    // Mata Kuliah
    @GET("matakuliah")
    suspend fun getMataKuliah(): RemoteResponse<List<RemoteMataKuliah>>

    @GET("matakuliah/materi")
    suspend fun getMateriKuliah(
        @Query("kode_mk") kodeMk: String? = null
    ): RemoteResponse<List<RemoteMateriKuliah>>

    @POST("matakuliah/upload-tugas")
    suspend fun uploadTugas(
        @Body request: RemoteUploadTugasRequest
    ): RemoteResponse<Any>

    // Kelas
    @GET("kelas/angkatan")
    suspend fun getAngkatan(): RemoteResponse<List<String>>

    @GET("kelas/by-angkatan/{angkatan}")
    suspend fun getKelasByAngkatan(
        @Path("angkatan") angkatan: String
    ): RemoteResponse<List<RemoteProdiKelas>>

    @GET("kelas/mahasiswa/{kodeKelas}")
    suspend fun getMahasiswaByKelas(
        @Path("kodeKelas") kodeKelas: String
    ): RemoteResponse<List<RemoteMahasiswaKelas>>

    // Mahasiswa
    @GET("mahasiswa/{nim}")
    suspend fun getMahasiswaDetail(
        @Path("nim") nim: String
    ): RemoteResponse<RemoteMahasiswa>

    @PUT("mahasiswa/{nim}")
    suspend fun updateMahasiswa(
        @Path("nim") nim: String,
        @Body request: RemoteUpdateMahasiswaRequest
    ): RemoteResponse<RemoteMahasiswa>

    @GET("mahasiswa/{nim}/nilai")
    suspend fun getMahasiswaNilai(
        @Path("nim") nim: String
    ): RemoteResponse<RemoteNilai>
} 