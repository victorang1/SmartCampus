package com.example.smartcampus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcampus.R

class ClassAdapter(
    private val onClassSelected: (String) -> Unit
) : RecyclerView.Adapter<ClassAdapter.ViewHolder>() {

    private var items = listOf<String>()

    fun setItems(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_class, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvClass: TextView = itemView.findViewById(R.id.tv_class_name)

        init {
            itemView.setOnClickListener {
                onClassSelected(items[adapterPosition])
            }
        }

        fun bind(className: String) {
            tvClass.text = className
        }
    }
} 