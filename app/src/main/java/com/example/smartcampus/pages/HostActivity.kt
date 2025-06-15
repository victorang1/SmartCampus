package com.example.smartcampus.pages

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.smartcampus.R
import com.example.smartcampus.fragments.AbsenceFragment
import com.example.smartcampus.fragments.CalendarFragment
import com.example.smartcampus.fragments.HomeFragment
import com.example.smartcampus.fragments.MateriFragment
import com.example.smartcampus.fragments.ProfileFragment

class HostActivity : AppCompatActivity() {
    private lateinit var fabAbsence: FrameLayout
    private lateinit var menuHome: LinearLayout
    private lateinit var menuMateri: LinearLayout
    private lateinit var menuCalendar: LinearLayout
    private lateinit var menuProfile: LinearLayout
    private lateinit var ivHome: ImageView
    private lateinit var ivMateri: ImageView
    private lateinit var ivCalendar: ImageView
    private lateinit var ivProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        initializeViews()
        setupBottomNavigation()
        loadFragment(HomeFragment())
    }

    private fun initializeViews() {
        menuHome = findViewById(R.id.menu_home)
        fabAbsence = findViewById(R.id.fab_absence)
        menuMateri = findViewById(R.id.menu_materi)
        menuCalendar = findViewById(R.id.menu_calendar)
        menuProfile = findViewById(R.id.menu_profile)
        ivMateri = findViewById(R.id.iv_materi)
        ivCalendar = findViewById(R.id.iv_calendar)
        ivProfile = findViewById(R.id.iv_profile)
        ivHome = findViewById(R.id.iv_home)
    }

    private fun setupBottomNavigation() {
        menuHome.setOnClickListener { onNavigationItemSelected(it) }
        menuMateri.setOnClickListener { onNavigationItemSelected(it) }
        menuCalendar.setOnClickListener { onNavigationItemSelected(it) }
        menuProfile.setOnClickListener { onNavigationItemSelected(it) }
        fabAbsence.setOnClickListener {
            loadFragment(AbsenceFragment())
        }
    }

    private fun onNavigationItemSelected(view: View) {
        resetIcons()

        when (view.id) {
            R.id.menu_home -> {
                ivHome.setImageResource(R.drawable.ic_home_selected)
                menuHome.layoutParams.width =
                    resources.getDimensionPixelSize(R.dimen.icon_size_selected)
                menuHome.layoutParams.height =
                    resources.getDimensionPixelSize(R.dimen.icon_size_selected)
                loadFragment(HomeFragment())
            }

            R.id.menu_materi -> {
                ivMateri.setImageResource(R.drawable.ic_materi_selected)
                menuMateri.layoutParams.width =
                    resources.getDimensionPixelSize(R.dimen.icon_size_selected)
                menuMateri.layoutParams.height =
                    resources.getDimensionPixelSize(R.dimen.icon_size_selected)
                loadFragment(MateriFragment())
            }

            R.id.menu_calendar -> {
                ivCalendar.setImageResource(R.drawable.ic_calendar_selected)
                menuCalendar.layoutParams.width =
                    resources.getDimensionPixelSize(R.dimen.icon_size_selected)
                menuCalendar.layoutParams.height =
                    resources.getDimensionPixelSize(R.dimen.icon_size_selected)
                loadFragment(CalendarFragment())
            }

            R.id.menu_profile -> {
                ivProfile.setImageResource(R.drawable.ic_profile_selected)
                menuProfile.layoutParams.width =
                    resources.getDimensionPixelSize(R.dimen.icon_size_selected)
                menuProfile.layoutParams.height =
                    resources.getDimensionPixelSize(R.dimen.icon_size_selected)
                loadFragment(ProfileFragment())
            }
        }
    }

    private fun resetIcons() {
        ivHome.setImageResource(R.drawable.ic_home_unselected)
        menuHome.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        menuHome.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

        ivMateri.setImageResource(R.drawable.ic_materi_unselected)
        menuMateri.layoutParams.width =
            resources.getDimensionPixelSize(R.dimen.icon_size_unselected)
        menuMateri.layoutParams.height =
            resources.getDimensionPixelSize(R.dimen.icon_size_unselected)

        ivCalendar.setImageResource(R.drawable.ic_calendar_unselected)
        menuCalendar.layoutParams.width =
            resources.getDimensionPixelSize(R.dimen.icon_size_unselected)
        menuCalendar.layoutParams.height =
            resources.getDimensionPixelSize(R.dimen.icon_size_unselected)

        ivProfile.setImageResource(R.drawable.ic_profile_unselected)
        menuProfile.layoutParams.width =
            resources.getDimensionPixelSize(R.dimen.icon_size_unselected)
        menuProfile.layoutParams.height =
            resources.getDimensionPixelSize(R.dimen.icon_size_unselected)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
} 