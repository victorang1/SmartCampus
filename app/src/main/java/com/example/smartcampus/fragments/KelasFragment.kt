package com.example.smartcampus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcampus.ApiClient
import com.example.smartcampus.R
import com.example.smartcampus.adapters.ProdiAdapter
import com.example.smartcampus.adapters.YearAdapter
import kotlinx.coroutines.launch

class KelasFragment : Fragment() {
    private lateinit var rvYears: RecyclerView
    private lateinit var rvClass: RecyclerView
    private lateinit var ivBack: ImageView
    private lateinit var yearAdapter: YearAdapter
    private lateinit var prodiAdapter: ProdiAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_class, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        setupRecyclerViews()
        loadYears()
    }

    private fun setupViews(view: View) {
        rvYears = view.findViewById(R.id.rv_years)
        rvClass = view.findViewById(R.id.rv_class)
        ivBack = view.findViewById(R.id.iv_back)

        ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerViews() {
        yearAdapter = YearAdapter { year ->
            loadClasses(year)
        }

        prodiAdapter = ProdiAdapter { kodeKelas ->
            navigateToListMahasiswa(kodeKelas)
        }

        rvYears.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = yearAdapter
        }

        rvClass.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = prodiAdapter
        }
    }

    private fun loadYears() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getAngkatan()
                if (response.success && response.data != null) {
                    yearAdapter.setItems(response.data)
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadClasses(year: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getKelasByAngkatan(year)
                if (response.success && response.data != null) {
                    prodiAdapter.setItems(response.data)
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToListMahasiswa(kodeKelas: String) {
        val fragment = ListMahasiswaFragment().apply {
            arguments = Bundle().apply {
                putString("kode_kelas", kodeKelas)
            }
        }

        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
} 