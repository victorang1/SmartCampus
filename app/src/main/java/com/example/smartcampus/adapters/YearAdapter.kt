package com.example.smartcampus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcampus.R

class YearAdapter(
    private val onYearSelected: (String) -> Unit
) : RecyclerView.Adapter<YearAdapter.ViewHolder>() {

    private var items = listOf<String>()
    private var selectedPosition = -1

    fun setItems(newItems: List<String>) {
        items = newItems
        selectedPosition = if (newItems.isNotEmpty()) 0 else -1
        notifyDataSetChanged()
        if (selectedPosition >= 0) {
            onYearSelected(items[selectedPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_year, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position == selectedPosition)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvYear: TextView = itemView as TextView

        init {
            itemView.setOnClickListener {
                val oldPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(oldPosition)
                notifyItemChanged(selectedPosition)
                onYearSelected(items[adapterPosition])
            }
        }

        fun bind(year: String, isSelected: Boolean) {
            tvYear.text = year
            tvYear.setBackgroundResource(if (isSelected) R.drawable.rounded_fourteen_primary else 0)
            tvYear.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (isSelected) R.color.white else R.color.black
                )
            )
        }
    }
} 