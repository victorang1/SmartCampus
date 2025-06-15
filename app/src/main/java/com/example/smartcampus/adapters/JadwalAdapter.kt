package com.example.smartcampus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcampus.R
import com.smartcampus.api.model.RemoteJadwal

class JadwalAdapter : RecyclerView.Adapter<JadwalAdapter.ViewHolder>() {

    private var items = listOf<RemoteJadwal>()

    fun setItems(newItems: List<RemoteJadwal>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvSubTitle: TextView = itemView.findViewById(R.id.tv_sub_title)
        private val tvStudent: TextView = itemView.findViewById(R.id.tv_student)
        private val tvLocation: TextView = itemView.findViewById(R.id.tv_location)
        private val tvSchedule: TextView = itemView.findViewById(R.id.tv_schedule)

        fun bind(item: RemoteJadwal) {
            tvTitle.text = item.kodeMk
            tvSubTitle.text = item.namaMk
            tvStudent.text = item.namaDosen
            tvLocation.text = item.ruang

            tvSchedule.text = "${item.jamMulai}\n${item.jamSelesai}"
        }
    }
} 