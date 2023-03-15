package com.training.finalproject.authentication

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.training.finalproject.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest

class OAuthViewModel() : ViewModel() {
    private val _user =
        User(email = "abc@xyz.com", password = "abcd12345", fullName = "Carol Richarmen", id = 10)
    val statusLogin = MutableLiveData<Boolean>()
    val statusRegister = MutableLiveData<Boolean>()
    val userAccount = MutableLiveData<User>()
    var token = MutableLiveData<Int>()


    fun checkLogin(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            if (user.email == _user.email && user.password == _user.password) {
                statusLogin.postValue(true)
                userAccount.postValue(_user)
            } else statusLogin.postValue(false)
        }
    }

    fun register(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val passwordHashed = md5(user.password)
            user.password = passwordHashed
            userAccount.postValue(user)
            statusRegister.postValue(true)
        }
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun rememberAccount(user: User, sharedPreferences: SharedPreferences) {
        viewModelScope.launch(Dispatchers.IO) {
            val edit = sharedPreferences.edit()
            val gson = Gson()
            val userInformation = gson.toJson(user)
            val hsaString = user.hashCode()
            edit.putString("RememberUser", userInformation)
            edit.putInt("hsaUser", hsaString)
            edit.apply()
        }
    }

    fun getRememberedAccount(sharedPreferences: SharedPreferences) {
        viewModelScope.launch(Dispatchers.IO) {
            val gson = Gson()
            val userInformation = sharedPreferences.getString("RememberUser", null)
            userInformation?.let {
                val user = gson.fromJson(it, User::class.java)
                userAccount.postValue(user)
            }
            val tokenNumber = sharedPreferences.getInt("hsaUser", 0)
            token.postValue(tokenNumber)
        }
    }
}
