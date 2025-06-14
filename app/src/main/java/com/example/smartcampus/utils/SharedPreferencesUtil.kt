package com.example.smartcampus.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.smartcampus.api.model.RemoteDosen
import com.smartcampus.api.model.RemoteMahasiswa

object SharedPreferencesUtil {
    private const val PREF_NAME = "SmartCampusPrefs"
    private const val KEY_USER_TYPE = "user_type"
    private const val KEY_USER_DATA = "user_data"
    private const val USER_TYPE_MAHASISWA = "mahasiswa"
    private const val USER_TYPE_DOSEN = "dosen"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveMahasiswaData(context: Context, mahasiswa: RemoteMahasiswa) {
        getPrefs(context).edit().apply {
            putString(KEY_USER_TYPE, USER_TYPE_MAHASISWA)
            putString(KEY_USER_DATA, Gson().toJson(mahasiswa))
            apply()
        }
    }

    fun saveDosenData(context: Context, dosen: RemoteDosen) {
        getPrefs(context).edit().apply {
            putString(KEY_USER_TYPE, USER_TYPE_DOSEN)
            putString(KEY_USER_DATA, Gson().toJson(dosen))
            apply()
        }
    }

    fun getMahasiswaData(context: Context): RemoteMahasiswa? {
        val userData = getPrefs(context).getString(KEY_USER_DATA, null)
        return if (userData != null && isUserMahasiswa(context)) {
            Gson().fromJson(userData, RemoteMahasiswa::class.java)
        } else null
    }

    fun isUserLoggedIn(context: Context): Boolean {
        return getPrefs(context).contains(KEY_USER_TYPE)
    }

    fun isUserMahasiswa(context: Context): Boolean {
        return getPrefs(context).getString(KEY_USER_TYPE, "") == USER_TYPE_MAHASISWA
    }

    fun logout(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
} 