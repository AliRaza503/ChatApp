package com.example.chat.onBoarding

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.chat.R
import com.example.chat.databinding.FragmentOnBoardingBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class OnBoarding : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var googleApiClient: GoogleSignInClient

    private lateinit var intent: Intent
    private lateinit var regActivity: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestProfile()
            .requestEmail()
            .build()
        googleApiClient = GoogleSignIn.getClient(requireActivity(), options)
        binding.oAuthContainerLayout.btnGoogleOAuth.setOnClickListener {
            signIn()
        }
        intent = googleApiClient.signInIntent
        regActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(
                            result.data
                        )
                    handleSignInResult(task)
                }
            }
        return binding.root

    }

    private fun signIn() {
        regActivity.launch(intent)
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
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