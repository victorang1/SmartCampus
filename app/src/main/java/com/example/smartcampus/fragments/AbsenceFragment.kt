package com.example.smartcampus.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
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
import com.example.smartcampus.R
import com.example.smartcampus.database.DataAccess
import com.example.smartcampus.utils.SharedPreferencesUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.CircleOptions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AbsenceFragment : Fragment(), OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sharedPreferencesUtil: SharedPreferencesUtil
    private lateinit var mMap: GoogleMap
    private lateinit var dataAccess: DataAccess
    
    // Views
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

    private val campusLatLng = LatLng(-7.275973, 112.790337)
    private val maxDistanceMeters = 100

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
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
        setupDatabase()
        setupMap()
        updateDateTime()
        loadUserData()
        setupClickListeners()
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        
        // Add campus marker
        mMap.addMarker(MarkerOptions()
            .position(campusLatLng)
            .title("Kampus"))

        // Add circle to show attendance radius
        mMap.addCircle(CircleOptions()
            .center(campusLatLng)
            .radius(maxDistanceMeters.toDouble())
            .strokeColor(ContextCompat.getColor(requireContext(), R.color.primary))
//            .fillColor(ContextCompat.getColor(requireContext(), R.color.primary_transparent))
        )

        // Move camera to campus
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(campusLatLng, 17f))

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
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLatLng))
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
    }

    private fun setupLocationServices() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        sharedPreferencesUtil = SharedPreferencesUtil(requireContext())
        
        if (hasLocationPermission()) {
            startLocationUpdates()
        } else {
            requestLocationPermission()
        }
    }

    private fun setupDatabase() {
        dataAccess = DataAccess()
    }

    private fun updateDateTime() {
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        val currentDate = dateFormat.format(Date())
        tvNotification.text = currentDate
    }

    private fun loadUserData() {
        sharedPreferencesUtil.getMahasiswaData()?.let { mahasiswa ->
            tvName.text = mahasiswa.nama
            tvNim.text = "NIM : ${mahasiswa.nim}"
        }
    }

    private fun setupClickListeners() {
        btnAbsen.setOnClickListener {
            if (hasLocationPermission()) {
                checkLocationAndMarkAttendance()
            } else {
                requestLocationPermission()
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
                    checkDistance(it)
                    updateCurrentLocation()
                }
            }
        }
    }

    private fun checkDistance(currentLocation: Location): Float {
        val campusLocation = Location("").apply {
            latitude = campusLatLng.latitude
            longitude = campusLatLng.longitude
        }
        return currentLocation.distanceTo(campusLocation)
    }

    private fun checkLocationAndMarkAttendance() {
        if (hasLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val distance = checkDistance(it)
                    if (distance <= maxDistanceMeters) {
                        markAttendance(it.latitude, it.longitude)
                    } else {
                        Toast.makeText(
                            context,
                            "Anda harus berada dalam radius ${maxDistanceMeters}m dari kampus",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun markAttendance(latitude: Double, longitude: Double) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        
        // Get current user's NIM and jadwal ID
        sharedPreferencesUtil.getMahasiswaData()?.let { mahasiswa ->
            // Get the jadwal ID from your views
            val jadwalId = tvTitle.text.toString()

            val query = """
                INSERT INTO absensi (nim, id_jadwal, tanggal, waktu_absen, status_kehadiran, latitude, longitude, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
            """.trimIndent()

            val params: Array<Any> = arrayOf(
                mahasiswa.nim,
                jadwalId,
                currentDate,
                currentTime,
                "hadir",
                latitude.toString(),
                longitude.toString()
            )

            dataAccess.executeUpdate(query, params, object : DataAccess.UpdateCallback {
                override fun onSuccess(rowsAffected: Int) {
                    requireActivity().runOnUiThread {
                        // Hide the attendance button
                        btnAbsen.visibility = View.GONE

                        // Show the attendance confirmation card
                        cvAlreadyAbsence.visibility = View.VISIBLE

                        // Update attendance time
                        tvAbsentDescription.text = "Anda sudah absen sebagai: HADIR"
                        tvAbsentDescription2.text = "Tercatat pukul: $currentTime WIB"

                        Toast.makeText(context, "Absensi berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(e: Exception) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            context,
                            "Gagal menyimpan absensi: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
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

    override fun onDestroy() {
        super.onDestroy()
        dataAccess.close()
    }
} 