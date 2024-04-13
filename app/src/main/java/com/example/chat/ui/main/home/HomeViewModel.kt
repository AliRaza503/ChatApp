package com.example.chat.ui.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.MainActivity
import com.example.chat.network.findEmail.FindEmailAPI
import com.example.chat.network.findEmail.RequestEmailDTO
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _findEmailResult = MutableLiveData<Boolean>()
    val findEmailResult: LiveData<Boolean> = _findEmailResult
    var email: String = ""
    fun findEmail(email: String) {
        viewModelScope.launch {
            try {
                val findEmailResponse = FindEmailAPI.retrofitService.findEmail(
                    email = email,
                    authToken = "Bearer ${MainActivity.JWT.value}"
                )
                _findEmailResult.value = findEmailResponse.found
                if (findEmailResponse.found) {
                    this@HomeViewModel.email = email
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "findEmail: ${e.printStackTrace()}, ${e.message}")
                _findEmailResult.value = false
            }
        }
    }
}