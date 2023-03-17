package com.training.finalproject.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.training.finalproject.R
import com.training.finalproject.home_activity.HomeActivity
import com.training.finalproject.utils.replaceFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private val viewModel: OAuthViewModel by activityViewModels()
    private val sharedPreferences by lazy {
        activity?.getSharedPreferences("saveInformation", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.getRememberedAccount(sharedPreferences!!)
            delay(3000)
            viewModel.token.observe(viewLifecycleOwner) { value ->
                if (value != 0) {
                    val intent = Intent(activity, HomeActivity::class.java)
                    val bundle = Bundle()
                    val user = viewModel.userAccount.value
                    bundle.putParcelable("userInformation", user)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    replaceFragment(LoginFragment(), R.id.loginContainer, true)
                }
            }
        }
    }
}