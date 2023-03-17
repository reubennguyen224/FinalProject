package com.training.finalproject.home_activity.drawer

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.finalproject.R
import com.training.finalproject.home_activity.allproducts.AllProductsFragment
import com.training.finalproject.home_activity.becomeseller.BecomeSellerFragment
import com.training.finalproject.home_activity.categories.CategoriesFragment
import com.training.finalproject.home_activity.dashboard.DashboardFragment
import com.training.finalproject.home_activity.helpcenter.HelpCenterFragment
import com.training.finalproject.home_activity.newrelease.NewReleaseFragment
import com.training.finalproject.home_activity.promotion.PromotionFragment
import com.training.finalproject.home_activity.settings.SettingsFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DrawerViewModel : ViewModel() {
    private val optionList = arrayListOf(
        OptionDrawer(R.drawable.ic_home, "Dashboard", true, DashboardFragment()),
        OptionDrawer(R.drawable.ic_become_seller, "Become Seller", false, BecomeSellerFragment()),
        OptionDrawer(R.drawable.ic_help_center, "Help Center", false, HelpCenterFragment()),
        OptionDrawer(R.drawable.ic_categories, "Categories", false, CategoriesFragment()),
        OptionDrawer(R.drawable.ic_all_product, "All Product", false, AllProductsFragment()),
        OptionDrawer(R.drawable.ic_new_release, "New Release", false, NewReleaseFragment()),
        OptionDrawer(R.drawable.ic_promotion, "Promotion", false, PromotionFragment()),
        OptionDrawer(R.drawable.ic_setting, "Settings", false, SettingsFragment())
    )
    private val list = Option()
    val optionListLiveData = MutableLiveData<Option>()

    init {
        list.addAll(optionList)
        optionListLiveData.postValue(list)
    }

    fun click(optionDrawer: OptionDrawer) {
        viewModelScope.launch(Dispatchers.IO) {
            for (obj in optionList) {
                obj.status = false
                if (obj.nameOption == optionDrawer.nameOption) obj.status = true
            }
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