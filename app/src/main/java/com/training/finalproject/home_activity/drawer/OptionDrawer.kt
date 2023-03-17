package com.training.finalproject.home_activity.drawer

import androidx.fragment.app.Fragment

data class OptionDrawer(
    val icon: Int,
    val nameOption: String,
    var status: Boolean,
    val fragment: Fragment
)

class Option : ArrayList<OptionDrawer>()