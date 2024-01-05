package com.example.chat.auth

import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

class FacebookAuthentication(
    private val fragment: Fragment,
    private val callbackManager: CallbackManager,
    private val onSuccess: (String) -> Unit,
    private val onCancel: () -> Unit,
    private val onError: (FacebookException) -> Unit
) {

    fun authenticate() {
        LoginManager.getInstance()
            .logInWithReadPermissions(fragment, callbackManager, listOf("email", "public_profile"))
    }

    fun registerCallback() {
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    onCancel.invoke()
                }

                override fun onError(error: FacebookException) {
                    onError.invoke(error)
                }

                override fun onSuccess(result: LoginResult) {
                    onSuccess.invoke(result.accessToken.token)
                }
            })
    }
}
