package com.example.smartcampus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcampus.R
import com.smartcampus.api.model.RemoteProdiKelas

class ProdiAdapter(
    private val onClassSelected: (String) -> Unit
) : RecyclerView.Adapter<ProdiAdapter.ViewHolder>() {

    private var items = listOf<RemoteProdiKelas>()

    fun setItems(newItems: List<RemoteProdiKelas>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prodi, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvProdi: TextView = itemView.findViewById(R.id.tv_prodi)
        private val rvClasses: RecyclerView = itemView.findViewById(R.id.rv_classes)
        private val classAdapter = ClassAdapter(onClassSelected)

        init {
            rvClasses.apply {
                layoutManager = GridLayoutManager(itemView.context, 4)
                if (itemDecorationCount == 0) {
                    addItemDecoration(SquareItemDecoration())
                }
                adapter = classAdapter
                
                // Force measure and layout after the view is laid out
                post {
                    adapter?.notifyDataSetChanged()
                }
            }
        }

        fun bind(item: RemoteProdiKelas) {
            tvProdi.text = item.prodi
            classAdapter.setItems(item.kelas)
        }
    }
} 