package com.training.finalproject.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.finalproject.R
import com.training.finalproject.model.Option
import com.training.finalproject.model.OptionDrawer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DrawerViewModel : ViewModel() {
    private val optionList = arrayListOf(
        OptionDrawer(R.drawable.ic_home, "Dashboard", true),
        OptionDrawer(R.drawable.ic_become_seller, "Become Seller", false),
        OptionDrawer(R.drawable.ic_help_center, "Help Center", false),
        OptionDrawer(R.drawable.ic_categories, "Categories", false),
        OptionDrawer(R.drawable.ic_all_product, "All Product", false),
        OptionDrawer(R.drawable.ic_new_release, "New Release", false),
        OptionDrawer(R.drawable.ic_promotion, "Promotion", false),
        OptionDrawer(R.drawable.ic_setting, "Settings", false)
    )
    private val list = Option()
    val optionListLiveData = MutableLiveData<Option>()

    init {
        list.addAll(optionList)
        optionListLiveData.postValue(list)
    }

    fun click(position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            for (obj in optionList) {
                obj.status = false
            }
            optionList[position].status = true
            list.clear()
            list.addAll(optionList)
            optionListLiveData.postValue(list)
        }
    }

    fun logout(sharedPreferences: SharedPreferences) {
        viewModelScope.launch(Dispatchers.IO) {
            sharedPreferences.edit().clear().apply()
        }
    }
}