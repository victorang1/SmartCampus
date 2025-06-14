package com.example.smartcampus.adapters

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcampus.R
import android.os.Bundle
import com.example.smartcampus.fragments.FragmentStudentProfile
import com.smartcampus.api.model.RemoteMahasiswaKelas
import de.hdodenhof.circleimageview.CircleImageView

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
        private val btnDetail: Button = itemView.findViewById(R.id.btn_detail)
        private val ivAvatar: CircleImageView = itemView.findViewById(R.id.iv_avatar)

        fun bind(item: RemoteMahasiswaKelas) {
            tvNim.text = item.nim
            tvName.text = item.nama
            tvDesc.text = kodeKelas

            item.fotoProfileUrl?.let { base64Image ->
                try {
                    if (base64Image.isNotEmpty()) {
                        val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        ivAvatar.setImageBitmap(bitmap)
                    }
                } catch (e: Exception) {
                }
            }
            
            btnDetail.setOnClickListener {
                val fragment = FragmentStudentProfile().apply {
                    arguments = Bundle().apply {
                        putString("nim", item.nim)
                    }
                }

                val activity = itemView.context as? AppCompatActivity
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.fragmentContainer, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
            }
        }
    }
} 