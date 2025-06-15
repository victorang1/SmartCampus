package com.example.smartcampus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcampus.R
import com.smartcampus.api.model.RemoteNilaiDetail

class ScoreAdapter : RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {
    private var items = listOf<RemoteNilaiDetail>()

    fun setItems(newItems: List<RemoteNilaiDetail>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_score, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCourse: TextView = itemView.findViewById(R.id.tv_course)
        private val tvScore: TextView = itemView.findViewById(R.id.tv_score)

        fun bind(item: RemoteNilaiDetail) {
            tvCourse.text = item.namaMk
            tvScore.text = "${item.sks} - ${item.nilaiHuruf}"
        }
    }
} 