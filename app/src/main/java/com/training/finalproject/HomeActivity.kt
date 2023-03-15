package com.training.finalproject

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.training.finalproject.databinding.ActivityHomeBinding
import com.training.finalproject.ui.dashboard.DashboardFragment
import com.training.finalproject.ui.shopping.CartFragment
import com.training.finalproject.utils.MyApplication
import com.training.finalproject.utils.replaceFragment
import com.training.finalproject.viewmodel.HomeFragmentViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityHomeBinding
    private val binding get() = _binding
    private val viewModel: HomeFragmentViewModel by viewModels()

    private lateinit var drawerLayout: DrawerLayout

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) = Unit

            override fun onDrawerOpened(drawerView: View) {
                replaceFragment(DrawerFragment(), R.id.drawerContainer, true)
            }

            override fun onDrawerClosed(drawerView: View) {
//                super.onDrawerClosed(drawerView)
//                supportFragmentManager.popBackStack()
            }

            override fun onDrawerStateChanged(newState: Int) = Unit

        })

        if (supportFragmentManager.findFragmentByTag("DashboardFragment") == null)
            replaceFragment(DashboardFragment(), R.id.fragmentContainer, true)

        viewModel.getRememberedAccount(getSharedPreferences("saveInformation", MODE_PRIVATE))
        viewModel.getHomeList()
        viewModel.getCart((application as MyApplication).repository)
    }

    fun openDrawer(){
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else
            super.onBackPressed()
    }

}