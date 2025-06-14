package com.example.smartcampus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.smartcampus.ApiClient
import com.example.smartcampus.R
import com.example.smartcampus.utils.SharedPreferencesUtil
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var tvName: TextView
    private lateinit var tvNim: TextView
    private lateinit var tvCourse: TextView
    private lateinit var llMahasiswa: LinearLayout


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        loadUserData()
        loadTodayClasses()
        setupClickListeners()
    }

    private fun initializeViews(view: View) {
        tvName = view.findViewById(R.id.tv_name)
        tvNim = view.findViewById(R.id.tv_nim)
        tvCourse = view.findViewById(R.id.tv_course)
        llMahasiswa = view.findViewById(R.id.ll_mahasiswa)
    }

    private fun loadUserData() {
        SharedPreferencesUtil.getMahasiswaData(requireContext())?.let { mahasiswa ->
            tvName.text = mahasiswa.nama
            tvNim.text = mahasiswa.nim
        }
    }

    private fun loadTodayClasses() {
        val dayFormat = SimpleDateFormat("EEEE", Locale("id", "ID"))
        val currentDay = dayFormat.format(Date())

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val mahasiswaData = SharedPreferencesUtil.getMahasiswaData(requireContext())
                val response = ApiClient.apiService.getJadwalByDay(mahasiswaData?.nim.orEmpty(), currentDay)

                if (response.success && response.data != null) {
                    val jadwalList = response.data
                    if (jadwalList.isNotEmpty()) {
                        val scheduleText = jadwalList.joinToString("\n") { jadwal ->
                            "${jadwal.jamMulai} - ${jadwal.jamSelesai} - ${jadwal.namaMk}"
                        }
                        tvCourse.text = scheduleText
                    } else {
                        tvCourse.text = "Tidak ada mata kuliah hari ini"
                    }
                } else {
                    tvCourse.text = "Gagal memuat jadwal: ${response.message}"
                }
            } catch (e: Exception) {
                tvCourse.text = "Error: ${e.message}"
            }
        }
    }

    private fun setupClickListeners() {
        llMahasiswa.setOnClickListener {
            loadFragment(KelasFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
} 