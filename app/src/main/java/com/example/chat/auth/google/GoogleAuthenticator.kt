package com.example.chat.auth.google

import android.content.Context
import android.os.Bundle
import androidx.credentials.CreateCredentialRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoogleAuthenticator(
    private val context: Context,
    clientId: String,
    nonce: String,
    rememberAccount: Boolean = false
) {

    private val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId(clientId)
        .setNonce(nonce)
        .setFilterByAuthorizedAccounts(rememberAccount)
        .setAutoSelectEnabled(true)
        .build()
    private val credentialManager: CredentialManager = CredentialManager.create(context)
    private val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .setCredentialOptions(listOf(googleIdOption))
        .build()

    fun login(
        onTokenIdReceived: (String) -> Unit,
        onDialogDismissed: (String) -> Unit
    ) {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            try {
                val response = credentialManager.getCredential(
                    request = request,
                    context = context
                )
                handleSignIn(
                    credentialResponse = response,
                    onTokenIdReceived = {
                        onTokenIdReceived(it)
                    },
                    onDialogDismissed = {
                        println(it)
                        onDialogDismissed(it)
                    }
                )
            } catch (e: NoCredentialException) {
//                credentialManager.createCredential(context, createCredentialRequest)
            } catch (e: GetCredentialException) {
                try {
                    val errorMessage = if (e.message != null) {
                        if (e.message!!.contains("activity is cancelled by the user.")) {
                            "Dialog Closed."
                        } else if (e.message!!.contains("Caller has been temporarily blocked")) {
                            "Sign in has been Temporarily Blocked due to too many Closed Prompts."
                        } else {
                            e.message.toString()
                        }
                    } else "Unknown Error."
                    println(errorMessage)
                    onDialogDismissed(errorMessage)
                } catch (e: Exception) {
                    println("${e.message}")
                    onDialogDismissed("${e.message}")
                }
            } catch (e: Exception) {
                println("${e.message}")
                onDialogDismissed("${e.message}")
            }
        }
    }


    private fun handleSignIn(
        credentialResponse: GetCredentialResponse,
        onTokenIdReceived: (String) -> Unit,
        onDialogDismissed: (String) -> Unit,
    ) {
        when (val credential = credentialResponse.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        onTokenIdReceived(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        onDialogDismissed("Invalid Google tokenId response: ${e.message}")
                    }
                } else {
                    onDialogDismissed("Unexpected Type of Credential.")
                }
            }

            else -> {
                onDialogDismissed("Unexpected Type of Credential.")
            }
        }
    }

    suspend fun isUserSignedIn(): Bundle? {
        // Check using credential manager if user is already logged in using Google
        return null
    }
}
