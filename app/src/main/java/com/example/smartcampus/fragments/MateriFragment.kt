package com.example.smartcampus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcampus.ApiClient
import com.example.smartcampus.R
import com.smartcampus.api.model.RemoteMataKuliah
import kotlinx.coroutines.launch

class MateriFragment : Fragment() {
    private lateinit var rvMateri: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var materiAdapter: MateriAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_materi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        setupRecyclerView()
        loadMataKuliah()
    }

    private fun initializeViews(view: View) {
        rvMateri = view.findViewById(R.id.rv_materi)
        etSearch = view.findViewById(R.id.et_search)
    }

    private fun setupRecyclerView() {
        materiAdapter = MateriAdapter { mataKuliah ->
            navigateToDetail(mataKuliah)
        }
        rvMateri.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = materiAdapter
        }
    }

    private fun navigateToDetail(mataKuliah: RemoteMataKuliah) {
        val detailFragment = MateriDetailFragment.newInstance(mataKuliah.kodeMk)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun loadMataKuliah() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getMataKuliah()
                if (response.success && response.data != null) {
                    materiAdapter.submitList(response.data)
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class MateriAdapter(
    private val onItemClick: (RemoteMataKuliah) -> Unit
) : RecyclerView.Adapter<MateriAdapter.MateriViewHolder>() {
    private var items = listOf<RemoteMataKuliah>()

    fun submitList(newItems: List<RemoteMataKuliah>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_materi, parent, false)
        return MateriViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: MateriViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class MateriViewHolder(
        itemView: View,
        private val onItemClick: (RemoteMataKuliah) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvCode = itemView.findViewById<TextView>(R.id.tv_code)
        private val tvCourse = itemView.findViewById<TextView>(R.id.tv_course)
        private val tvLecturer = itemView.findViewById<TextView>(R.id.tv_lecturer)

        fun bind(item: RemoteMataKuliah) {
            tvCode.text = item.kodeMk
            tvCourse.text = item.namaMk
            tvLecturer.text = item.namaDosen

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }
} 