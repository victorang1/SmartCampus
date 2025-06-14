package com.example.smartcampus.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.smartcampus.R
import com.example.smartcampus.databinding.FragmentStudentProfileBinding
import com.example.smartcampus.models.User
import com.example.smartcampus.repository.SmartCampusRepository
import kotlinx.coroutines.launch

class StudentProfileFragment : Fragment() {
    private var _binding: FragmentStudentProfileBinding? = null
    private val binding get() = _binding!!
    private val repository = SmartCampusRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadProfile()
    }

    private fun loadProfile() {
        lifecycleScope.launch {
            try {
                // Get token from SharedPreferences or similar storage
                val token = "your_stored_token" // Replace with actual token retrieval
                val response = repository.getUserProfile(token)
                
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        updateUI(user)
                    }
                } else {
                    Toast.makeText(context, "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(user: User) {
        binding.apply {
            tvName.text = user.name
            tvNim.text = user.nim
            tvStatus.text = user.status
            tvPhone.text = user.phone
            tvEmail.text = user.email
            tvAddress.text = user.address
            tvReligion.text = user.religion
            tvProgramStudi.text = user.programStudi
            tvJurusan.text = user.jurusan
            tvAngkatan.text = user.angkatan.toString()
            tvDosen.text = user.dosenPA
            tvOrmawa.text = user.organizations.joinToString("\n")
            tvPrestasi.text = user.achievements.joinToString("\n") { "• $it" }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 