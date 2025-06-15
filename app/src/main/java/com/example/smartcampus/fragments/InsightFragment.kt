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
import com.example.smartcampus.adapters.ScoreAdapter
import com.example.smartcampus.utils.SharedPreferencesUtil
import com.example.smartcampus.views.IPKProgressView
import kotlinx.coroutines.launch

class InsightFragment : Fragment() {
    private lateinit var ipkProgress: IPKProgressView
    private lateinit var rvScores: RecyclerView
    private lateinit var scoreAdapter: ScoreAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_insight, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        setupRecyclerView()
        loadData()

        view.findViewById<ImageView>(R.id.iv_back)?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupViews(view: View) {
        ipkProgress = view.findViewById(R.id.ipk_progress)
        rvScores = view.findViewById(R.id.rv_scores)
    }

    private fun setupRecyclerView() {
        scoreAdapter = ScoreAdapter()
        rvScores.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = scoreAdapter
        }
    }

    private fun loadData() {
        val mahasiswaData = SharedPreferencesUtil.getMahasiswaData(requireContext())
        mahasiswaData?.let { data ->
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val response = ApiClient.apiService.getMahasiswaNilai(data.nim)
                    if (response.success && response.data != null) {
                        ipkProgress.setProgress(response.data.ipk.toFloat())
                        scoreAdapter.setItems(response.data.nilai)
                    } else {
                        Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
} 