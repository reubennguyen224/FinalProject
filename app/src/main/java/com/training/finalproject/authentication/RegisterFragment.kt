package com.training.finalproject.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.training.finalproject.R
import com.training.finalproject.databinding.FragmentRegisterBinding
import com.training.finalproject.model.User
import com.training.finalproject.utils.replaceFragment

class RegisterFragment : Fragment() {

    private lateinit var _binding: FragmentRegisterBinding
    private val binding get() = _binding
    private val viewModel: OAuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignIn.setOnClickListener {
            replaceFragment(LoginFragment(), R.id.loginContainer, true)
        }

        viewModel.statusRegister.observe(viewLifecycleOwner) {
            if (it == true) {
                parentFragmentManager.popBackStack()
            }
        }

        binding.btnSignUp.setOnClickListener {
            val userFullName = binding.txtFullNameInput.editText?.text.toString()
            val phoneNumber = binding.txtPhoneInput.editText?.text.toString()
            val password = binding.txtPasswordInput.editText?.text.toString()
            val passwordConfirm = binding.txtPasswordConfirmInput.editText?.text.toString()
            val email = binding.txtEmailInput.editText?.text.toString()
            if (password.length < 8)
                binding.txtPasswordInput.error = "Password must have at least 8 character!"
            if (!password.matches(Regex("(.*\\d.*)")))
                binding.txtPasswordInput.error = "Password must contain at least 1 number!"
            if (!password.matches(Regex("(.*[A-Z].*)")))
                binding.txtPasswordInput.error =
                    "Password must contain at least 1 upper case letter!"
            if (!password.matches(Regex("^(?=.*[_.()\$&@]).*\$")))
                binding.txtPasswordInput.error = "Password must contain at least 1 symbol!"
            if (password == passwordConfirm) {
                viewModel.register(
                    User(
                        email = email,
                        password = password,
                        fullName = userFullName,
                        phoneNumber = phoneNumber
                    )
                )
            } else {
                binding.txtPasswordInput.error = "Passwords do not match!"
                binding.txtPasswordConfirmInput.error = "Passwords do not match!"
            }
        }
    }

}