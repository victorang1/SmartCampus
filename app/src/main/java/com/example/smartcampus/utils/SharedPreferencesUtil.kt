package com.example.smartcampus.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.smartcampus.models.Dosen
import com.example.smartcampus.models.Mahasiswa
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class SharedPreferencesUtil(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson: Gson = GsonBuilder().create()

    companion object {
        private const val PREF_NAME = "SmartCampusPrefs"
        private const val KEY_USER_TYPE = "user_type"
        private const val KEY_USER_DATA = "user_data"
        private const val USER_TYPE_MAHASISWA = "mahasiswa"
        private const val USER_TYPE_DOSEN = "dosen"
    }

    fun saveMahasiswaData(mahasiswa: Mahasiswa) {
        with(sharedPreferences.edit()) {
            putString(KEY_USER_TYPE, USER_TYPE_MAHASISWA)
            putString(KEY_USER_DATA, gson.toJson(mahasiswa))
            apply()
        }
    }

    fun saveDosenData(dosen: Dosen) {
        with(sharedPreferences.edit()) {
            putString(KEY_USER_TYPE, USER_TYPE_DOSEN)
            putString(KEY_USER_DATA, gson.toJson(dosen))
            apply()
        }
    }

    fun getMahasiswaData(): Mahasiswa? {
        val userData = sharedPreferences.getString(KEY_USER_DATA, null)
        return if (userData != null && isUserMahasiswa()) {
            gson.fromJson(userData, Mahasiswa::class.java)
        } else null
    }

    fun getDosenData(): Dosen? {
        val userData = sharedPreferences.getString(KEY_USER_DATA, null)
        return if (userData != null && isUserDosen()) {
            gson.fromJson(userData, Dosen::class.java)
        } else null
    }

    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.contains(KEY_USER_TYPE)
    }

    fun isUserMahasiswa(): Boolean {
        return sharedPreferences.getString(KEY_USER_TYPE, "") == USER_TYPE_MAHASISWA
    }

    fun isUserDosen(): Boolean {
        return sharedPreferences.getString(KEY_USER_TYPE, "") == USER_TYPE_DOSEN
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }
} 