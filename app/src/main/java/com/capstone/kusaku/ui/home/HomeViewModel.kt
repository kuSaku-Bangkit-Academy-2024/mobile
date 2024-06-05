package com.capstone.kusaku.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _userName = MutableLiveData<String>().apply {
        value = "Hello, Fadhlan Hasyim" // Default value, or you can load from a repository
    }
    val userName: LiveData<String> = _userName

    // Function to update user name
    fun setUserName(name: String) {
        _userName.value = "Hello, $name"
    }
}