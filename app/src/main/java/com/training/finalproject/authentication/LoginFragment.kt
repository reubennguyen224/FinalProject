package com.training.finalproject.authentication

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.training.finalproject.R
import com.training.finalproject.databinding.FragmentLoginBinding
import com.training.finalproject.home_activity.HomeActivity
import com.training.finalproject.model.User
import com.training.finalproject.utils.replaceFragment

class LoginFragment : Fragment() {
    private lateinit var _binding: FragmentLoginBinding
    private val binding get() = _binding
    private val viewModel: OAuthViewModel by activityViewModels()
    private val sharedPreferences by lazy {
        activity?.getSharedPreferences("saveInformation", MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel.getRememberedAccount(sharedPreferences!!)
        viewModel.token.observe(viewLifecycleOwner) { value ->
            if (value != 0) {
                val intent = Intent(activity, HomeActivity::class.java)
                val bundle = Bundle()
                val user = viewModel.userAccount.value
                bundle.putParcelable("userInformation", user)
                intent.putExtras(bundle)
                startActivity(intent)
                activity?.finish()
            }
        }

        viewModel.userAccount.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.txtEmailInput.editText?.setText(it.email)
                binding.txtPasswordInput.editText?.setText(it.password)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.statusLogin.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(context, "Login Success!", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity?.baseContext, HomeActivity::class.java)
                if (binding.chkRememberMe.isChecked)
                    viewModel.rememberAccount(
                        User(
                            email = binding.txtEmailInput.editText?.text.toString(),
                            password = binding.txtPasswordInput.editText?.text.toString()
                        ), sharedPreferences!!
                    )
                val user = viewModel.userAccount.value
                intent.putExtra("userInformation", user)
                startActivity(intent)
                activity?.finish()
            } else
                Toast.makeText(context, "Login Failed!", Toast.LENGTH_LONG).show()
        }

        binding.btnSignIn.setOnClickListener {
            val userMail = binding.txtEmailInput.editText?.text.toString()
            val password = binding.txtPasswordInput.editText?.text.toString()
            viewModel.checkLogin(User(email = userMail, password = password))

        }

        binding.btnSignUp.setOnClickListener {
            replaceFragment(RegisterFragment(), R.id.loginContainer, true)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("email", binding.txtEmailInput.editText?.text.toString())
        outState.putString("password", binding.txtPasswordInput.editText?.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            binding.txtEmailInput.editText?.setText(it.getString("email", ""))
            binding.txtPasswordInput.editText?.setText(it.getString("password", ""))
        }
    }
}