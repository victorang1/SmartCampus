package com.example.smartcampus.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.smartcampus.ApiClient
import com.example.smartcampus.R
import com.example.smartcampus.utils.SharedPreferencesUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.smartcampus.api.model.RemoteAbsenRequest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AbsenceFragment : Fragment(), OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    
    private lateinit var tvWelcome: TextView
    private lateinit var tvName: TextView
    private lateinit var tvNim: TextView
    private lateinit var tvNotification: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvSubTitle: TextView
    private lateinit var tvStudentName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvTime: TextView
    private lateinit var btnAbsen: Button
    private lateinit var cvAlreadyAbsence: CardView
    private lateinit var tvAbsentDescription: TextView
    private lateinit var tvAbsentDescription2: TextView

    private var currentJadwalId: Int? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val DEFAULT_ZOOM = 17f
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_absence, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initializeViews(view)
        setupLocationServices()
        setupMap()
        updateDateTime()
        loadUserData()
        loadCurrentJadwal()
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (hasLocationPermission()) {
            enableMyLocation()
        }
    }

    private fun enableMyLocation() {
        if (hasLocationPermission() && ::mMap.isInitialized) {
            mMap.isMyLocationEnabled = true
            updateCurrentLocation()
        }
    }

    private fun updateCurrentLocation() {
        if (hasLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM))
                }
            }
        }
    }

    private fun initializeViews(view: View) {
        tvWelcome = view.findViewById(R.id.tv_welcome)
        tvName = view.findViewById(R.id.tv_name)
        tvNim = view.findViewById(R.id.tv_nim)
        tvNotification = view.findViewById(R.id.tv_notification)
        tvTitle = view.findViewById(R.id.tv_title)
        tvSubTitle = view.findViewById(R.id.tv_sub_title)
        tvStudentName = view.findViewById(R.id.tv_student_name)
        tvDescription = view.findViewById(R.id.tv_description)
        tvTime = view.findViewById(R.id.tv_time)
        btnAbsen = view.findViewById(R.id.btn_absen)
        cvAlreadyAbsence = view.findViewById(R.id.cv_already_absence)
        tvAbsentDescription = view.findViewById(R.id.tv_absent_description)
        tvAbsentDescription2 = view.findViewById(R.id.tv_absent_description_2)

        btnAbsen.setOnClickListener {
            if (hasLocationPermission()) {
                markAttendance()
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun setupLocationServices() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        
        if (hasLocationPermission()) {
            startLocationUpdates()
        } else {
            requestLocationPermission()
        }
    }

    private fun updateDateTime() {
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        val currentDate = dateFormat.format(Date())
        tvNotification.text = currentDate
    }

    private fun loadUserData() {
        SharedPreferencesUtil.getMahasiswaData(requireContext())?.let { mahasiswa ->
            tvName.text = mahasiswa.nama
            tvNim.text = "NIM : ${mahasiswa.nim}"
        }
    }

    private fun loadCurrentJadwal() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val mahasiswaData = SharedPreferencesUtil.getMahasiswaData(requireContext())
                val response = ApiClient.apiService.getCurrentJadwal(mahasiswaData?.nim.orEmpty())
                if (response.success && response.data != null) {
                    val jadwal = response.data
                    currentJadwalId = jadwal.idJadwal
                    
                    tvTitle.text = jadwal.kodeMk
                    tvSubTitle.text = jadwal.namaMk
                    tvStudentName.text = jadwal.namaDosen
                    tvDescription.text = jadwal.ruang
                    tvTime.text = "${jadwal.jamMulai} - ${jadwal.jamSelesai}"

                    checkAbsensiStatus()
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkAbsensiStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val mahasiswaData = SharedPreferencesUtil.getMahasiswaData(requireContext())
                if (mahasiswaData != null && currentJadwalId != null) {
                    val response = ApiClient.apiService.checkAbsensi(mahasiswaData.nim, currentJadwalId!!)
                    if (response.success && response.data != null) {
                        val absensi = response.data
                        if (absensi.hasAttended) {
                            btnAbsen.visibility = View.GONE
                            cvAlreadyAbsence.visibility = View.VISIBLE
                            tvAbsentDescription.text = "Anda sudah absen sebagai: ${absensi.attendance?.statusKehadiran?.uppercase()}"
                            tvAbsentDescription2.text = "Tercatat pukul: ${absensi.attendance?.waktuAbsen} WIB"

                            // Update map with attendance location
                            absensi.attendance?.let { attendance ->
                                val attendanceLocation = LatLng(attendance.latitude, attendance.longitude)
                                mMap.clear()
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(attendanceLocation)
                                        .title("Lokasi Absen")
                                        .snippet("${attendance.waktuAbsen} WIB")
                                )
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(attendanceLocation, DEFAULT_ZOOM))
                            }
                        } else {
                            btnAbsen.visibility = View.VISIBLE
                            cvAlreadyAbsence.visibility = View.GONE
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun startLocationUpdates() {
        if (hasLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    updateCurrentLocation()
                }
            }
        }
    }

    private fun markAttendance() {
        if (hasLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    submitAbsensi(it.latitude, it.longitude)
                }
            }
        }
    }

    private fun submitAbsensi(latitude: Double, longitude: Double) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val mahasiswaData = SharedPreferencesUtil.getMahasiswaData(requireContext())
                if (mahasiswaData != null && currentJadwalId != null) {
                    val request = RemoteAbsenRequest(
                        nim = mahasiswaData.nim,
                        idJadwal = currentJadwalId!!,
                        latitude = latitude,
                        longitude = longitude,
                        statusKehadiran = "hadir"
                    )

                    val response = ApiClient.apiService.submitAbsensi(request)
                    if (response.success) {
                        Toast.makeText(context, "Absensi berhasil", Toast.LENGTH_SHORT).show()
                        checkAbsensiStatus()
                    } else {
                        Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
                enableMyLocation()
            } else {
                Toast.makeText(
                    context,
                    "Izin lokasi diperlukan untuk absensi",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
} 