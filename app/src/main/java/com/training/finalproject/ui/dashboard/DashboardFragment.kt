package com.training.finalproject.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayout
import com.training.finalproject.R
import com.training.finalproject.databinding.FragmentDashboardBinding
import com.training.finalproject.ui.shopping.HomeFragment
import com.training.finalproject.utils.MyApplication
import com.training.finalproject.utils.findFragment
import com.training.finalproject.utils.replaceFragment
import com.training.finalproject.viewmodel.HomeFragmentViewModel

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        viewModel.setRepository((activity?.application as MyApplication).repository)
        if (parentFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName) == null)
            replaceFragment(HomeFragment(), R.id.dashboardContainer, true)
        else {
            val fm = findFragment(HomeFragment::class.java.simpleName)
            if (fm != null) {
                replaceFragment(fm, R.id.dashboardContainer)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = binding.bottomNavigation
        tabLayout.apply {
            addTab(tabLayout.newTab().setText(R.string.home).setIcon(R.drawable.ic_home), true)
            addTab(tabLayout.newTab().setText(R.string.feeds).setIcon(R.drawable.ic_feeds))
            addTab(
                tabLayout.newTab().setText(R.string.transaction).setIcon(R.drawable.ic_transaction)
            )
            addTab(tabLayout.newTab().setText(R.string.my_profile).setIcon(R.drawable.ic_myprofile))

        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> replaceFragmentFromBottomNav(HomeFragment())
                    1 -> replaceFragmentFromBottomNav(FeedsFragment())
                    2 -> replaceFragmentFromBottomNav(TransactionFragment())
                    3 -> replaceFragmentFromBottomNav(MyProfileFragment())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

            override fun onTabReselected(tab: TabLayout.Tab?) = Unit

        })


    }

    override fun onResume() {
        super.onResume()
        replaceFragmentFromBottomNav(HomeFragment())
    }

    fun replaceFragmentFromBottomNav(fragment: Fragment) {
        if (parentFragmentManager.findFragmentByTag(fragment.tag) == null) {
            replaceFragment(fragment, R.id.dashboardContainer, true)
        } else {
            replaceFragment(
                parentFragmentManager.findFragmentByTag(fragment.tag)!!,
                R.id.dashboardContainer,
                true
            )
        }
    }
}