package com.example.smartcampus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcampus.R
import com.smartcampus.api.model.RemoteMahasiswaKelas

class MahasiswaAdapter(val kodeKelas: String) : RecyclerView.Adapter<MahasiswaAdapter.ViewHolder>() {

    private var items = listOf<RemoteMahasiswaKelas>()

    fun setItems(newItems: List<RemoteMahasiswaKelas>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNim: TextView = itemView.findViewById(R.id.tv_nim)
        private val tvName: TextView = itemView.findViewById(R.id.tv_name)
        private val tvDesc: TextView = itemView.findViewById(R.id.tv_desc)

        fun bind(item: RemoteMahasiswaKelas) {
            tvNim.text = item.nim
            tvName.text = item.nama
            tvDesc.text = kodeKelas
        }
    }
} 