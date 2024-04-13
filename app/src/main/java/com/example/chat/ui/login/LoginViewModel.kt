package com.example.chat.ui.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.MainActivity
import com.example.chat.R
import com.example.chat.data.login.AuthAPIResponse
import com.example.chat.network.login.LoginAPI
import com.example.chat.network.login.LoginRequestDTO
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<AuthAPIResponse>()
    val loginResult: LiveData<AuthAPIResponse> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val loginRequestDTO = LoginRequestDTO(email, password)
                val loginResponse = LoginAPI.retrofitService.login(loginRequestDTO)
                _loginResult.value = loginResponse
            } catch (e: Exception) {
                Log.e("LoginViewModel", "login: ${e.printStackTrace()}, ${e.message}")
                _loginResult.value = AuthAPIResponse("", null)
            }
        }

    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}