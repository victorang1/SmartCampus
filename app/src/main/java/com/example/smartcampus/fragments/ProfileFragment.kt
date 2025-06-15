package com.example.smartcampus.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.smartcampus.ApiClient
import com.example.smartcampus.R
import com.example.smartcampus.pages.SplashActivity
import com.example.smartcampus.utils.SharedPreferencesUtil
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var tvName: TextView
    private lateinit var tvNim: TextView
    private lateinit var tvDesc: TextView
    private lateinit var ivAvatar: CircleImageView
    private lateinit var llPersonalInfo: LinearLayout
    private lateinit var llInsight: LinearLayout
    private lateinit var llLogout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        loadProfile()
    }

    private fun setupViews(view: View) {
        tvName = view.findViewById(R.id.tv_name)
        tvNim = view.findViewById(R.id.tv_nim)
        tvDesc = view.findViewById(R.id.tv_desc)
        ivAvatar = view.findViewById(R.id.iv_avatar)
        llPersonalInfo = view.findViewById(R.id.ll_personal_info)
        llInsight = view.findViewById(R.id.ll_insight)
        llLogout = view.findViewById(R.id.ll_logout)

        llPersonalInfo.setOnClickListener {
            loadFragment(PersonalInfoMahasiswaFragment())
        }

        llInsight.setOnClickListener {
            loadFragment(InsightFragment())
        }

        llLogout.setOnClickListener {
            SharedPreferencesUtil.logout(requireContext())
            val intent = Intent(requireContext(), SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun loadProfile() {
        val mahasiswaData = SharedPreferencesUtil.getMahasiswaData(requireContext())
        mahasiswaData?.let { data ->
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val response = ApiClient.apiService.getMahasiswaDetail(data.nim)
                    if (response.success && response.data != null) {
                        val mahasiswa = response.data
                        tvName.text = mahasiswa.nama
                        tvNim.text = mahasiswa.nim
                        tvDesc.text = mahasiswa.kodeKelas

                        if (mahasiswa.fotoProfileUrl.isNotEmpty()) {
                            try {
                                val imageBytes = Base64.decode(mahasiswa.fotoProfileUrl, Base64.DEFAULT)
                                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                ivAvatar.setImageBitmap(bitmap)
                            } catch (e: Exception) {
                                Toast.makeText(requireContext(), "Error loading profile image", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
} 