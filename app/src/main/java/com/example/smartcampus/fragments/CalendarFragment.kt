package com.example.smartcampus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcampus.ApiClient
import com.example.smartcampus.R
import com.example.smartcampus.adapters.JadwalAdapter
import com.example.smartcampus.utils.SharedPreferencesUtil
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarFragment : Fragment() {
    private lateinit var calendarView: CalendarView
    private lateinit var tvDayName: TextView
    private lateinit var tvMonthYear: TextView
    private lateinit var rvSchedules: RecyclerView
    private lateinit var tvNoSchedule: TextView
    private lateinit var jadwalAdapter: JadwalAdapter
    private var nim: String = ""
    private val dayFormat = SimpleDateFormat("EEEE", Locale("id"))
    private val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale("id"))
    
    private fun getDayName(date: Date): String {
        return dayFormat.format(date).capitalize()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mahasiswaData = SharedPreferencesUtil.getMahasiswaData(requireContext())
        nim = mahasiswaData?.nim.orEmpty()
        setupViews(view)
    }

    private fun setupViews(view: View) {
        calendarView = view.findViewById(R.id.calendar_view)
        tvDayName = view.findViewById(R.id.tv_day_name)
        tvMonthYear = view.findViewById(R.id.tv_month_year)
        rvSchedules = view.findViewById(R.id.rv_schedules)
        tvNoSchedule = view.findViewById(R.id.tv_no_schedule)

        jadwalAdapter = JadwalAdapter()
        rvSchedules.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = jadwalAdapter
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val date = calendar.time
            
            updateDateDisplay(date)
            loadJadwalForDate(getDayName(date))
        }

        val currentDate = Date()
        updateDateDisplay(currentDate)
        loadJadwalForDate(getDayName(currentDate))
    }

    private fun updateDateDisplay(date: Date) {
        tvDayName.text = getDayName(date)
        tvMonthYear.text = monthYearFormat.format(date).capitalize()
    }

    private fun loadJadwalForDate(date: String) {
        if (nim.isEmpty()) return

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getJadwalByDay(nim, date)
                if (response.success && response.data != null && response.data.isNotEmpty()) {
                    jadwalAdapter.setItems(response.data)
                    rvSchedules.visibility = View.VISIBLE
                    tvNoSchedule.visibility = View.GONE
                } else {
                    jadwalAdapter.setItems(emptyList())
                    rvSchedules.visibility = View.GONE
                    tvNoSchedule.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                jadwalAdapter.setItems(emptyList())
                rvSchedules.visibility = View.GONE
                tvNoSchedule.visibility = View.VISIBLE
            }
        }
    }
} 