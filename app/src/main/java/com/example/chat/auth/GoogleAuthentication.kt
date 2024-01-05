package com.example.chat.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleAuthentication(private val registerForActivityResult: ActivityResultLauncher<Intent>) {
    private lateinit var intent: Intent
    private lateinit var googleApiClient: GoogleSignInClient

    constructor(
        registerForActivityResult: ActivityResultLauncher<Intent>,
        context: Context
    ) : this(registerForActivityResult) {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestProfile()
            .requestEmail()
            .build()
        googleApiClient = GoogleSignIn.getClient(context, options)
        intent = googleApiClient.signInIntent
    }

    fun authenticate() {
        registerForActivityResult.launch(intent)
    }
}