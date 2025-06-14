package com.example.smartcampus.adapters

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartcampus.R
import com.smartcampus.api.model.RemoteMateriKuliah

class MateriDetailAdapter(
    private val onUploadClick: (RemoteMateriKuliah) -> Unit
) : RecyclerView.Adapter<MateriDetailAdapter.ViewHolder>() {

    private var items = listOf<RemoteMateriKuliah>()

    fun setItems(newItems: List<RemoteMateriKuliah>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_materi_detail, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvFiles: TextView = itemView.findViewById(R.id.tv_files)
        private val ivUpload: ImageView = itemView.findViewById(R.id.iv_upload)
        private val ivFileType: ImageView = itemView.findViewById(R.id.iv_file_type)

        fun bind(item: RemoteMateriKuliah) {
            tvTitle.text = item.judulMateri
            tvFiles.text = item.deskripsi

            val fileIconUrl = when (item.tipeFile.lowercase()) {
                "doc", "docx" -> "https://static2.sharepointonline.com/files/fabric/assets/brand-icons/product/svg/word_48x1.svg"
                "xls", "xlsx" -> "https://static2.sharepointonline.com/files/fabric/assets/brand-icons/product/svg/excel_48x1.svg"
                "ppt", "pptx" -> "https://static2.sharepointonline.com/files/fabric/assets/brand-icons/product/svg/powerpoint_48x1.svg"
                "pdf" -> "https://ssl.gstatic.com/docs/doclist/images/icon_11_pdf_list.png"
                else -> "https://ssl.gstatic.com/docs/doclist/images/icon_11_generic_list.png"
            }
            
            Glide.with(itemView.context)
                .load(fileIconUrl)
                .error(R.drawable.ic_image_error)
                .into(ivFileType)
            
            ivUpload.setOnClickListener {
                onUploadClick(item)
            }

            tvFiles.setOnClickListener {
                if (item.fileUrl.isNotEmpty()) {
                    downloadFile(itemView.context, item)
                } else {
                    Toast.makeText(itemView.context, "File tidak tersedia", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun downloadFile(context: Context, item: RemoteMateriKuliah) {
            try {
                val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val uri = Uri.parse(item.fileUrl)

                val fileName = item.fileUrl.substringAfterLast("/")
                val request = DownloadManager.Request(uri).apply {
                    setTitle(item.judulMateri)
                    setDescription("Downloading ${item.deskripsi}")
                    setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                    setAllowedOverMetered(true)
                    setAllowedOverRoaming(true)
                }

                downloadManager.enqueue(request)
                Toast.makeText(context, "Downloading file...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
} 