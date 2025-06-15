package com.example.smartcampus.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.smartcampus.ApiClient
import com.example.smartcampus.R
import com.example.smartcampus.utils.SharedPreferencesUtil
import com.smartcampus.api.model.RemoteMahasiswa
import com.smartcampus.api.model.RemoteUpdateMahasiswaRequest
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class PersonalInfoMahasiswaFragment : Fragment() {
    private lateinit var etName: EditText
    private lateinit var etNim: EditText
    private lateinit var etKelas: EditText
    private lateinit var etBirthDate: EditText
    private lateinit var etJenisKelamin: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var etAddress: EditText
    private lateinit var etAgama: EditText
    private lateinit var etProgramStudi: EditText
    private lateinit var etFakultas: EditText
    private lateinit var etAngkatan: EditText
    private lateinit var etStatus: EditText
    private lateinit var etOrmawa: EditText
    private lateinit var etPrestasi: EditText
    private lateinit var btnSave: Button
    private lateinit var ivAvatar: CircleImageView
    private var selectedImageBase64: String? = null

    private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private val updateDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private val displayDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    loadImage(uri)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal_info_mahasiswa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        loadMahasiswaData()
    }

    private fun setupViews(view: View) {
        etName = view.findViewById(R.id.et_name)
        etNim = view.findViewById(R.id.et_nim)
        etKelas = view.findViewById(R.id.et_kelas)
        etBirthDate = view.findViewById(R.id.et_birth_date)
        etJenisKelamin = view.findViewById(R.id.et_jenis_kelamin)
        etPhone = view.findViewById(R.id.et_phone)
        etEmail = view.findViewById(R.id.et_email)
        etAddress = view.findViewById(R.id.et_address)
        etAgama = view.findViewById(R.id.et_agama)
        etProgramStudi = view.findViewById(R.id.et_program_studi)
        etFakultas = view.findViewById(R.id.et_fakultas)
        etAngkatan = view.findViewById(R.id.et_angkatan)
        etStatus = view.findViewById(R.id.et_status)
        etOrmawa = view.findViewById(R.id.et_ormawa)
        etPrestasi = view.findViewById(R.id.et_prestasi)
        btnSave = view.findViewById(R.id.btn_save)
        ivAvatar = view.findViewById(R.id.iv_avatar)

        btnSave.setOnClickListener {
            updateMahasiswaData()
        }

        ivAvatar.setOnClickListener {
            openImagePicker()
        }
    }

    private fun formatDateForDisplay(apiDate: String): String {
        return try {
            val date = apiDateFormat.parse(apiDate)
            date?.let { displayDateFormat.format(it) } ?: ""
        } catch (e: Exception) {
            apiDate
        }
    }

    private fun formatDateForApi(displayDate: String): String {
        return try {
            val date = displayDateFormat.parse(displayDate)
            date?.let { updateDateFormat.format(it) } ?: displayDate
        } catch (e: Exception) {
            displayDate
        }
    }

    private fun openImagePicker() {
        try {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            pickImage.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error opening gallery: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadImage(uri: Uri) {
        try {
            requireContext().contentResolver.openInputStream(uri)?.let { inputStream ->
                val bufferedInputStream = java.io.BufferedInputStream(inputStream)

                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                bufferedInputStream.mark(bufferedInputStream.available())
                BitmapFactory.decodeStream(bufferedInputStream, null, options)
                bufferedInputStream.reset()

                options.apply {
                    inJustDecodeBounds = false
                    inSampleSize = calculateInSampleSize(this, 512, 512)
                }

                val bitmap = BitmapFactory.decodeStream(bufferedInputStream, null, options)
                bitmap?.let {
                    val compressedBitmap = getResizedBitmap(it, 512)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
                    val imageBytes = byteArrayOutputStream.toByteArray()

                    val imageSizeInMB = imageBytes.size / (1024.0 * 1024.0)
                    if (imageSizeInMB > 1.0) {
                        Toast.makeText(
                            requireContext(),
                            "Gambar terlalu besar, silakan pilih gambar yang lebih kecil",
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }

                    selectedImageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                    ivAvatar.setImageBitmap(compressedBitmap)

                    // Cleanup
                    if (it != compressedBitmap) {
                        it.recycle()
                    }
                }
                
                // Close streams
                bufferedInputStream.close()
                inputStream.close()
            }
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Error loading image: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun getResizedBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        var width = bitmap.width
        var height = bitmap.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    private fun loadMahasiswaData() {
        val mahasiswaData = SharedPreferencesUtil.getMahasiswaData(requireContext())
        mahasiswaData?.let { data ->
            etName.setText(data.nama)
            etNim.setText(data.nim)
            etKelas.setText(data.kodeKelas)
            etBirthDate.setText(formatDateForDisplay(data.tanggalLahir))
            etJenisKelamin.setText(data.jenisKelamin)
            etPhone.setText(data.noTelepon)
            etEmail.setText(data.email)
            etAddress.setText(data.alamatDomisili)
            etAgama.setText(data.agama)
            etProgramStudi.setText(data.programStudi)
            etFakultas.setText(data.jurusan)
            etAngkatan.setText(data.angkatan)
            etStatus.setText(data.statusMahasiswa)
            etOrmawa.setText(data.ormawa)
            etPrestasi.setText(data.riwayatPrestasi)

            if (data.fotoProfileUrl.isNotEmpty()) {
                try {
                    val imageBytes = Base64.decode(data.fotoProfileUrl, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    ivAvatar.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Error loading profile image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateMahasiswaData() {
        val mahasiswaData = SharedPreferencesUtil.getMahasiswaData(requireContext())
        mahasiswaData?.let { data ->
            val request = RemoteUpdateMahasiswaRequest(
                nama = etName.text.toString(),
                email = etEmail.text.toString(),
                tanggalLahir = formatDateForApi(etBirthDate.text.toString()),
                jenisKelamin = etJenisKelamin.text.toString(),
                noTelepon = etPhone.text.toString(),
                alamatDomisili = etAddress.text.toString(),
                agama = etAgama.text.toString(),
                programStudi = etProgramStudi.text.toString(),
                jurusan = etFakultas.text.toString(),
                angkatan = etAngkatan.text.toString(),
                statusMahasiswa = etStatus.text.toString(),
                ormawa = etOrmawa.text.toString(),
                riwayatPrestasi = etPrestasi.text.toString(),
                kodeKelas = etKelas.text.toString(),
                fotoProfileUrl = selectedImageBase64
            )

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = ApiClient.apiService.updateMahasiswa(data.nim, request)
                    withContext(Dispatchers.Main) {
                        if (response.success) {
                            SharedPreferencesUtil.saveMahasiswaData(
                                requireContext(),
                                response.data as RemoteMahasiswa
                            )
                            Toast.makeText(
                                requireContext(),
                                "Data berhasil diperbarui",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Gagal memperbarui data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
} 