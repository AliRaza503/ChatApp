package com.example.chat.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.data.login.AuthAPIResponse
import com.example.chat.network.signup.SignupAPI
import com.example.chat.network.signup.SignupRequestDTO
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {
    private val _signupResult = MutableLiveData<AuthAPIResponse>()
    val signupResult: LiveData<AuthAPIResponse> = _signupResult

    fun signup(userName: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            try {
                val signupResponse = SignupAPI.retrofitService.register(
                    SignupRequestDTO(
                        userName,
                        email,
                        password,
                        confirmPassword
                    )
                )
                _signupResult.value = signupResponse

            } catch (e: Exception) {
                _signupResult.value = AuthAPIResponse("", null)
            }
        }
    }
}