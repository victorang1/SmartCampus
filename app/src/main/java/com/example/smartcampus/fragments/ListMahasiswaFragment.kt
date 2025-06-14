package com.example.smartcampus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcampus.ApiClient
import com.example.smartcampus.R
import com.example.smartcampus.adapters.MahasiswaAdapter
import com.smartcampus.api.model.RemoteMahasiswaKelas
import kotlinx.coroutines.launch

class ListMahasiswaFragment : Fragment() {
    private lateinit var rvStudents: RecyclerView
    private lateinit var ivBack: ImageView
    private lateinit var btnKodeKelas: Button
    private var kodeKelas: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kodeKelas = arguments?.getString("kode_kelas")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mahasiswa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        loadMahasiswa()
    }

    private fun setupViews(view: View) {
        rvStudents = view.findViewById(R.id.rv_students)
        ivBack = view.findViewById(R.id.iv_back)
        btnKodeKelas = view.findViewById(R.id.btn_kode_kelas)

        btnKodeKelas.text = kodeKelas.orEmpty()

        view.findViewById<ImageView>(R.id.iv_back)?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        view.findViewById<TextView>(R.id.tv_class)?.text = "Daftar Mahasiswa"
        rvStudents.apply {
            layoutManager = GridLayoutManager(context, 3)
        }
    }

    private fun loadMahasiswa() {
        if (kodeKelas == null) {
            Toast.makeText(context, "Kode kelas tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getMahasiswaByKelas(kodeKelas!!)
                if (response.success && response.data != null) {
                    setupMahasiswaList(response.data)
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupMahasiswaList(mahasiswaList: List<RemoteMahasiswaKelas>) {
        val adapter = MahasiswaAdapter(kodeKelas.orEmpty())
        adapter.setItems(mahasiswaList)
        rvStudents.adapter = adapter
    }
} 