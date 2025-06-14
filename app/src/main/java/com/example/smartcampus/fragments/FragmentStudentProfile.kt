package com.example.smartcampus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.smartcampus.ApiClient
import com.example.smartcampus.R
import kotlinx.coroutines.launch

class FragmentStudentProfile : Fragment() {
    private lateinit var tvName: TextView
    private lateinit var tvNim: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvJurusan: TextView
    private lateinit var tvProgramStudi: TextView
    private lateinit var tvDosen: TextView
    private lateinit var tvRiwayatOrganisasi: TextView
    private lateinit var tvPrestasi: TextView
    private var nim: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nim = arguments?.getString("nim")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        loadProfile()
    }

    private fun setupViews(view: View) {
        tvName = view.findViewById(R.id.tv_name)
        tvNim = view.findViewById(R.id.tv_nim)
        tvDesc = view.findViewById(R.id.tv_desc)
        tvJurusan = view.findViewById(R.id.tv_jurusan)
        tvProgramStudi = view.findViewById(R.id.tv_program_studi)
        tvDosen = view.findViewById(R.id.tv_dosen)
        tvRiwayatOrganisasi = view.findViewById(R.id.tv_riwayat_organisasi)
        tvPrestasi = view.findViewById(R.id.rv_prestasi)

        view.findViewById<ImageView>(R.id.iv_back)?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun loadProfile() {
        if (nim == null) {
            Toast.makeText(context, "NIM tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getMahasiswaDetail(nim!!)
                if (response.success && response.data != null) {
                    val mahasiswa = response.data
                    tvName.text = mahasiswa.nama
                    tvNim.text = mahasiswa.nim
                    tvDesc.text = mahasiswa.kodeKelas
                    tvJurusan.text = mahasiswa.jurusan
                    tvProgramStudi.text = mahasiswa.programStudi
//                    tvDosen.text = mahasiswa.dosenPa
//                    tvRiwayatOrganisasi.text = mahasiswa.riwayatOrganisasi
//                    tvPrestasi.text = mahasiswa.riwayatPrestasi
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
} 