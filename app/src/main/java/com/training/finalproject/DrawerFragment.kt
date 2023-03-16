package com.training.finalproject

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.finalproject.adapter.DrawerOptionAdapter
import com.training.finalproject.databinding.FragmentDrawerBinding
import com.training.finalproject.viewmodel.DrawerViewModel
import com.training.finalproject.viewmodel.HomeFragmentViewModel


class DrawerFragment : Fragment() {

    private var drawerOptionAdapter = DrawerOptionAdapter()

    private var _binding: FragmentDrawerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DrawerViewModel by activityViewModels()
    private val sharingViewModel: HomeFragmentViewModel by viewModels()
    private val sharedPreferences by lazy {
        activity?.getSharedPreferences("saveInformation", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = Color.WHITE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentDrawerBinding.inflate(inflater, container, false)
        }
        viewModel.optionListLiveData.observe(viewLifecycleOwner) {
            drawerOptionAdapter.differ.submitList(it)
            binding.listOption.adapter = drawerOptionAdapter
        }
        drawerOptionAdapter.setOnClickListener(object : DrawerOptionAdapter.OnClickListener {
            override fun onClick(position: Int) {
                viewModel.click(position)
                Toast.makeText(requireContext(), "Tada + $position", Toast.LENGTH_SHORT).show()
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listOption.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = drawerOptionAdapter
            setHasFixedSize(true)
        }
        binding.headerDrawer.btnClose.setOnClickListener {

        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout(sharedPreferences!!)
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        sharingViewModel.userAccountLiveData.observe(viewLifecycleOwner) {
            binding.headerDrawer.txtBalance.text = it.balance.toString()
            binding.headerDrawer.txtPoints.text = it.point.toString()
        }
    }

}