package com.training.finalproject.authentication

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.training.finalproject.R

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<OAuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (supportFragmentManager.findFragmentByTag("splashFragment") == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.loginContainer, SplashFragment(), "splashFragment")
                .addToBackStack("splashFragment").commit()
        viewModel.getRememberedAccount(getSharedPreferences("saveInformation", MODE_PRIVATE))
    }

}