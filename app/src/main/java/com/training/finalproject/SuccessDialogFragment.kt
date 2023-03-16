package com.training.finalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.training.finalproject.databinding.FragmentSuccessDialogBinding

class SuccessDialogFragment : DialogFragment() {

    private var _binding: FragmentSuccessDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessDialogBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnOK.setOnClickListener {
            dismiss()
        }
    }
}