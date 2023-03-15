package com.training.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.training.finalproject.authentication.LoginFragment
import com.training.finalproject.authentication.OAuthViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<OAuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (supportFragmentManager.findFragmentByTag("login_fragment") == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.loginContainer, LoginFragment(), "login_fragment")
                .addToBackStack("login_fragment").commit()
        viewModel.getRememberedAccount(getSharedPreferences("saveInformation", MODE_PRIVATE))
    }

}