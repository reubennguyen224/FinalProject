package com.training.finalproject.home_activity.drawer

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
import com.training.finalproject.R
import com.training.finalproject.home_activity.HomeActivity
import com.training.finalproject.authentication.MainActivity
import com.training.finalproject.databinding.FragmentDrawerBinding
import com.training.finalproject.utils.BaseFragment
import com.training.finalproject.utils.replaceFragment
import com.training.finalproject.viewmodel.HomeFragmentViewModel


class DrawerFragment : BaseFragment<FragmentDrawerBinding>(
    FragmentDrawerBinding::inflate
) {

    private var drawerOptionAdapter = DrawerOptionAdapter()

    private val viewModel: DrawerViewModel by activityViewModels()
    private val sharingViewModel: HomeFragmentViewModel by activityViewModels { HomeFragmentViewModel.Factory }
    private val sharedPreferences by lazy {
        activity?.getSharedPreferences("saveInformation", Context.MODE_PRIVATE)
    }

    override fun setupView() {
        drawerOptionAdapter.onClick = {
            viewModel.click(it)
            replaceFragment(it.fragment, R.id.fragmentContainer, true)
            (activity as HomeActivity).closeDrawer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = Color.WHITE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.optionListLiveData.observe(viewLifecycleOwner) {
            drawerOptionAdapter.differ.submitList(it)
            binding.listOption.adapter = drawerOptionAdapter
        }

        binding.listOption.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = drawerOptionAdapter
            setHasFixedSize(true)
        }
        binding.headerDrawer.btnClose.setOnClickListener {
            (activity as HomeActivity).closeDrawer()
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