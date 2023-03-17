package com.training.finalproject.home_activity.drawer

data class OptionDrawer(val icon: Int, val nameOption: String, var status: Boolean)
class Option : ArrayList<OptionDrawer>()