package com.example.smartcampus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcampus.ApiClient
import com.example.smartcampus.R
import com.example.smartcampus.adapters.MateriDetailAdapter
import kotlinx.coroutines.launch

class MateriDetailFragment : Fragment() {
    private lateinit var rvMateri: RecyclerView
    private lateinit var ivBack: ImageView
    private lateinit var adapter: MateriDetailAdapter
    private var kodeMk: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kodeMk = arguments?.getString("kode_mk")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_materi_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews(view)
        setupRecyclerView()
        loadData()
    }

    private fun setupViews(view: View) {
        rvMateri = view.findViewById(R.id.rv_materi)
        ivBack = view.findViewById(R.id.iv_back)

        ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        adapter = MateriDetailAdapter { materi ->
            val uploadFragment = UploadTugasFragment().apply {
                arguments = Bundle().apply {
                    putString("kode_mk", kodeMk)
                    putString("judul_materi", materi.judulMateri)
                    putString("judul_tugas", materi.deskripsi)
                }
            }

            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, uploadFragment)
                .addToBackStack(null)
                .commit()
        }

        rvMateri.layoutManager = LinearLayoutManager(requireContext())
        rvMateri.adapter = adapter
    }

    private fun loadData() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getMateriKuliah(kodeMk)
                if (response.success) {
                    adapter.setItems(response.data.orEmpty())
                } else {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newInstance(kodeMk: String) = MateriDetailFragment().apply {
            arguments = Bundle().apply {
                putString("kode_mk", kodeMk)
            }
        }
    }
} 