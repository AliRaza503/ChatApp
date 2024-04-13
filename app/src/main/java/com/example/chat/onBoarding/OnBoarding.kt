package com.example.chat.onBoarding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.chat.R
import com.example.chat.auth.FacebookAuthentication
import com.example.chat.auth.google.GoogleAuthenticator
import com.example.chat.auth.google.getUserFromTokenId
import com.example.chat.databinding.FragmentOnBoardingBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class OnBoarding : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var callbackManager: CallbackManager
    private lateinit var facebookAuth: FacebookAuthentication

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)

        val googleAuthenticator = GoogleAuthenticator(
            context = requireContext(),
            clientId = getString(R.string.google_auth_web_clientid),
            nonce = getString(R.string.google_auth_nonce),
            rememberAccount = false
        )
        isUserSignedIn()

//        val googleAuth =
//            GoogleAuthentication(
//                registerForActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                    if (result.resultCode == Activity.RESULT_OK) {
//                        val task: Task<GoogleSignInAccount> =
//                            GoogleSignIn.getSignedInAccountFromIntent(
//                                result.data
//                            )
//                        handleGoogleAuthResult(task)
//                    }
//                },
//                context = requireActivity()
//            )
//        binding.oAuthContainerLayout.btnGoogleOAuth.setOnClickListener {
//            googleAuth.authenticate()
//        }

        // Google Login
        binding.oAuthContainerLayout.btnGoogleOAuth.setOnClickListener {
            googleAuthenticator.login(
                onTokenIdReceived = { tokenId ->
                    Log.d("TAG", "onCreateView: $tokenId")
                    getUserFromTokenId(tokenId)?.let { user ->
                        Log.d("TAG", "onCreateView: $user ${user.expirationTime}")
                    }
                },
                onDialogDismissed = { message ->
                    Log.d("TAG", "onCreateView: $message")
                }
            )
        }

        // Facebook Login
        callbackManager = CallbackManager.Factory.create()

        facebookAuth = FacebookAuthentication(
            fragment = this,
            callbackManager = callbackManager,
            onSuccess = { token ->
                Log.d("TAG", "onSuccess: $token")
            },
            onCancel = {
                // Handle onCancel logic
            },
            onError = { error ->
                // Handle onError logic
            }
        )
        facebookAuth.registerCallback()

        binding.oAuthContainerLayout.btnFb.setOnClickListener {
            facebookAuth.authenticate()
        }
        return binding.root
    }

    private fun isUserSignedIn() {
        // Check if user is already logged in using Google
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account != null && !account.isExpired) {
            // TODO: Navigate to HomeActivity
            Log.d("TAG", "onCreateView: ${account.email}")
        }
        // Check if user is already logged in using Facebook
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            // TODO: Navigate to HomeActivity
            Log.d("TAG", "onCreateView: ${accessToken.token}")
        }
    }

    private fun handleGoogleAuthResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = task.getResult(
                ApiException::class.java
            )
            if (account != null) {
                // Signed in successfully, show authenticated UI.
                // updateUI(account)
                Log.d("TAG", "handleSignInResult: ${account.email}")
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            // Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            // updateUI(null)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigate to LoginFragment
        binding.btnLogin.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_onBoarding_to_loginFragment)
        }

        // Navigate to SignupFragment
        binding.btnSignupWithEmail.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_onBoarding_to_signupFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}