package com.example.smartcampus.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.smartcampus.ApiClient
import com.example.smartcampus.R
import com.example.smartcampus.utils.SharedPreferencesUtil
import com.smartcampus.api.model.RemoteUploadTugasRequest
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class UploadTugasFragment : Fragment() {
    private lateinit var btnUpload: Button
    private lateinit var ivBack: ImageView
    private var selectedFileUri: Uri? = null
    private var kodeMk: String? = null
    private var judulTugas: String? = null
    private var judulMateri: String? = null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedFileUri = uri
                    val fileName = getFileName(uri)
                    btnUpload.text = "Upload: $fileName"
                }
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                openFilePicker()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission required to upload files",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kodeMk = arguments?.getString("kode_mk")
        judulTugas = arguments?.getString("judul_tugas")
        judulMateri = arguments?.getString("judul_materi")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload_tugas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    private fun setupViews(view: View) {
        btnUpload = view.findViewById(R.id.btn_upload)
        ivBack = view.findViewById(R.id.iv_back)

        ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        btnUpload.setOnClickListener {
            if (selectedFileUri == null) {
                checkAndRequestPermissions()
            } else {
                uploadFile()
            }
        }
    }

    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissions = arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO
            )
            requestPermissions(permissions)
        } else {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            requestPermissions(permissions)
        }
    }

    private fun requestPermissions(permissions: Array<String>) {
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isEmpty()) {
            openFilePicker()
        } else {
            requestPermissionLauncher.launch(permissionsToRequest)
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        getContent.launch(intent)
    }

    private fun getFileName(uri: Uri): String {
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        } ?: "Unknown File"
    }

    private fun uploadFile() {
        val uri = selectedFileUri ?: return
        val fileName = getFileName(uri)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                btnUpload.isEnabled = false
                btnUpload.text = "Uploading..."

                val mahasiswaData = SharedPreferencesUtil.getMahasiswaData(requireContext())

                if (mahasiswaData == null) {
                    btnUpload.text = "Upload: $fileName"
                    Toast.makeText(requireContext(), "Gagal Upload File", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val request = RemoteUploadTugasRequest(
                    kodeMk = kodeMk.orEmpty(),
                    judulTugas = judulTugas.orEmpty(),
                    nim = mahasiswaData.nim,
                    kodeKelas = mahasiswaData.kodeKelas,
                    namaMk = judulMateri.orEmpty(),
                    fileTugas = fileName,
                    statusTugas = "menunggu"
                )

                val response = ApiClient.apiService.uploadTugas(request)
                if (response.success) {
                    Toast.makeText(requireContext(), "Upload successful", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    btnUpload.isEnabled = true
                    btnUpload.text = "Upload: $fileName"
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                btnUpload.isEnabled = true
                btnUpload.text = "Upload: $fileName"
            }
        }
    }

    private fun createTempFile(uri: Uri): File {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("upload", null, requireContext().cacheDir)
        tempFile.deleteOnExit()

        inputStream?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }

        return tempFile
    }
} 