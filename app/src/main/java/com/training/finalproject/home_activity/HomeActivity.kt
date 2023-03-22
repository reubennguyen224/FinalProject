package com.training.finalproject.home_activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.training.finalproject.R
import com.training.finalproject.databinding.ActivityHomeBinding
import com.training.finalproject.home_activity.dashboard.DashboardFragment
import com.training.finalproject.home_activity.drawer.DrawerFragment
import com.training.finalproject.utils.replaceFragment
import com.training.finalproject.home_activity.dashboard.home.HomeFragmentViewModel
import com.training.finalproject.home_activity.dashboard.shopping.cart.viewmodel.CartViewModel
import com.training.finalproject.utils.popBackStackInclusive

class HomeActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityHomeBinding
    private val binding get() = _binding
    private val viewModel: HomeFragmentViewModel by viewModels{ HomeFragmentViewModel.Factory}
    private val cartViewModel: CartViewModel by viewModels { CartViewModel.Factory }

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
            }

            override fun onDrawerStateChanged(newState: Int) = Unit

        })

        if (supportFragmentManager.findFragmentByTag("DashboardFragment") == null)
            replaceFragment(DashboardFragment(), R.id.fragmentContainer, true)

        viewModel.getRememberedAccount(getSharedPreferences("saveInformation", MODE_PRIVATE))

        viewModel.getCart()
    }

    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
        window?.statusBarColor = resources.getColor(R.color.white)
    }

    fun closeDrawer(){
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        window?.statusBarColor = resources.getColor(R.color.colorStatus)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else
            super.onBackPressed()
    }

}