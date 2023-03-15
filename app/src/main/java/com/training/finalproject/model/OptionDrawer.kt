package com.training.finalproject.model

data class OptionDrawer(val icon: Int, val nameOption: String, var status: Boolean)
class Option: ArrayList<OptionDrawer>()