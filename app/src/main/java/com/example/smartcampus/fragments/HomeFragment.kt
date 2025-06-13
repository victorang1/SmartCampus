package com.example.smartcampus.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.smartcampus.R
import com.example.smartcampus.database.DataAccess
import com.example.smartcampus.database.MahasiswaDataAccess
import com.example.smartcampus.utils.SharedPreferencesUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.sql.ResultSet

class HomeFragment : Fragment() {
    private lateinit var tvName: TextView
    private lateinit var tvNim: TextView
    private lateinit var tvCourse: TextView
    private lateinit var llMahasiswa: LinearLayout
    private lateinit var sharedPreferencesUtil: SharedPreferencesUtil
    private lateinit var mahasiswaDataAccess: MahasiswaDataAccess
    private val TAG = "HomeFragment"

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
        mahasiswaDataAccess = MahasiswaDataAccess()
        setupDatabase()
        loadUserData()
        loadTodayClasses()
        setupClickListeners()
        loadHomeData()
    }

    private fun initializeViews(view: View) {
        tvName = view.findViewById(R.id.tv_name)
        tvNim = view.findViewById(R.id.tv_nim)
        tvCourse = view.findViewById(R.id.tv_course)
        llMahasiswa = view.findViewById(R.id.ll_mahasiswa)
    }

    private fun setupDatabase() {
        sharedPreferencesUtil = SharedPreferencesUtil(requireContext())
    }

    private fun loadUserData() {
        sharedPreferencesUtil.getMahasiswaData()?.let { mahasiswa ->
            tvName.text = mahasiswa.nama
            tvNim.text = "NIM : ${mahasiswa.nim}"
        }
    }

    private fun loadTodayClasses() {
        viewLifecycleOwner.lifecycleScope.launch {
            sharedPreferencesUtil.getMahasiswaData()?.let { mahasiswa ->
                val dayFormat = SimpleDateFormat("EEEE", Locale("id", "ID"))
                val currentDay = dayFormat.format(Date())

                val query = """
                SELECT mk.nama_mk, jk.jam_mulai, jk.jam_selesai, jk.ruang
                FROM jadwal_kuliah jk
                JOIN mata_kuliah mk ON jk.id_mk = mk.id_mk
                WHERE jk.hari = ?
                AND jk.kelas IN (
                    SELECT kelas.id_kelas
                    FROM kelas
                    WHERE kelas.prodi = (
                        SELECT mahasiswa.program_studi
                        FROM mahasiswa
                        WHERE mahasiswa.nim = ?
                    )
                    AND kelas.angkatan = (
                        SELECT mahasiswa.angkatan
                        FROM mahasiswa
                        WHERE mahasiswa.nim = ?
                    )
                )
                ORDER BY jk.jam_mulai
            """.trimIndent()

                Log.d("<RESULT>", currentDay)

                mahasiswaDataAccess.executeQuery(
                    query,
                    arrayOf(currentDay, mahasiswa.nim, mahasiswa.nim),
                    object : DataAccess.QueryCallback {
                        override fun onSuccess(resultSet: ResultSet) {
                            val courseList = StringBuilder()
                            var hasClasses = false

                            while (resultSet.next()) {
                                hasClasses = true
                                val namaMk = resultSet.getString("nama_mk")
                                val jamMulai = resultSet.getString("jam_mulai")
                                val jamSelesai = resultSet.getString("jam_selesai")
                                val ruang = resultSet.getString("ruang")

                                courseList.append("$namaMk\n")
                                courseList.append("$jamMulai - $jamSelesai\n")
                                courseList.append("Ruang: $ruang\n\n")
                            }

                            requireActivity().runOnUiThread {
                                if (hasClasses) {
                                    tvCourse.text = courseList.toString().trim()
                                } else {
                                    tvCourse.text = "Tidak ada mata kuliah hari ini"
                                }
                            }
                        }

                        override fun onError(e: Exception) {
                            requireActivity().runOnUiThread {
                                tvCourse.text = "Gagal memuat jadwal kuliah"
                                Toast.makeText(
                                    context,
                                    "Error: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                )
            }
        }
    }

    private fun loadHomeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Example query - modify according to your needs
                val query = """
                    SELECT m.nim, m.nama, m.program_studi, mk.nama_mk, jk.hari, jk.jam_mulai, jk.jam_selesai
                    FROM mahasiswa m
                    JOIN jadwal_kuliah jk ON m.program_studi = jk.prodi
                    JOIN mata_kuliah mk ON jk.id_mk = mk.id_mk
                    WHERE m.nim = ?
                    ORDER BY jk.hari, jk.jam_mulai
                """.trimIndent()

                // Get NIM from SharedPreferences
                val sharedPrefs = requireActivity().getSharedPreferences("user_data", 0)
                val nim = sharedPrefs.getString("id", "") ?: ""

                withContext(Dispatchers.IO) {
                    mahasiswaDataAccess.executeQuery(query, arrayOf(nim), object : DataAccess.QueryCallback {
                        override fun onSuccess(resultSet: ResultSet) {
                            // Process the results in the main thread
                            lifecycleScope.launch(Dispatchers.Main) {
                                try {
                                    val scheduleList = mutableListOf<String>()
                                    while (resultSet.next()) {
                                        val mataKuliah = resultSet.getString("nama_mk")
                                        val hari = resultSet.getString("hari")
                                        val jamMulai = resultSet.getString("jam_mulai")
                                        val jamSelesai = resultSet.getString("jam_selesai")
                                        
                                        scheduleList.add("$mataKuliah\n$hari, $jamMulai - $jamSelesai")
                                    }
                                    
                                    // Update your UI here with scheduleList
                                    updateScheduleUI(scheduleList)
                                    
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error processing result set", e)
                                    showError("Error processing data: ${e.message}")
                                }
                            }
                        }

                        override fun onError(e: Exception) {
                            lifecycleScope.launch(Dispatchers.Main) {
                                Log.e(TAG, "Database query error", e)
                                showError("Database error: ${e.message}")
                            }
                        }
                    })
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in loadHomeData", e)
                showError("Error: ${e.message}")
            }
        }
    }

    private fun updateScheduleUI(scheduleList: List<String>) {
        // TODO: Update your UI components here
        // Example:
        // recyclerView.adapter = ScheduleAdapter(scheduleList)
        Log.d(TAG, "Schedule loaded: ${scheduleList.size} items")
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun setupClickListeners() {
        llMahasiswa.setOnClickListener {
            // TODO: Implement navigation to student profile or relevant screen
            Toast.makeText(context, "Fitur akan segera hadir", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mahasiswaDataAccess.close()
    }
} 