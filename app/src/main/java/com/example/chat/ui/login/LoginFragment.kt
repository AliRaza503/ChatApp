package com.example.chat.ui.login

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.chat.MainActivity
import com.example.chat.R
import com.example.chat.auth.FacebookAuthentication
import com.example.chat.auth.google.GoogleAuthenticator
import com.example.chat.auth.google.getUserFromTokenId
import com.example.chat.databinding.FragmentLoginBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var callbackManager: CallbackManager
    private lateinit var facebookAuth: FacebookAuthentication
    private lateinit var googleAuthenticator: GoogleAuthenticator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        googleAuthenticator = GoogleAuthenticator(
            context = requireContext(),
            clientId = getString(R.string.google_auth_web_clientid),
            nonce = getString(R.string.google_auth_nonce),
            rememberAccount = false
        )
        isUserSignedIn()

        // Google Login
        binding.oAuthContainerLayout.btnGoogleOAuth.setOnClickListener {
            googleAuthenticator.login(
                onTokenIdReceived = { tokenId ->
                    Log.d("TAG", "onCreateView: $tokenId")
                    getUserFromTokenId(tokenId)?.let { user ->
                        Log.d("TAG", "onCreateView: $user ${user.expirationTime}")
                    }
                    saveTokenToSharedPrefs(tokenId)
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

    private fun saveTokenToSharedPrefs(tokenId: String) {
        // Save the token to shared preferences
        val sharedPref = requireActivity().getPreferences(MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.google_auth_token), tokenId)
            apply()
        }
    }

    private fun isUserSignedIn() {
        // Check if user is already logged in using Google
        lifecycleScope.launch {
            val bundle = googleAuthenticator.isUserSignedIn()
            if (bundle?.isEmpty == false) {
                // TODO: Navigate to HomeActivity
                Log.d("TAG", "onCreateView: $bundle")
            }
        }
        // Check if user is already logged in using Facebook
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            // TODO: Navigate to HomeActivity
            Log.d("TAG", "onCreateView: ${accessToken.token}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        val usernameEditText = binding.emailET
        val passwordEditText = binding.passwordET
        val loginButton = binding.btnLogin
        val loadingProgressBar = binding.loading

        // navigate up click listener
        binding.navigateUp.setOnClickListener {
            binding.root.findNavController().navigateUp()
        }

        // navigate to register fragment
        binding.btnSignup.setOnClickListener {
            binding.root.findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
        }

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                Log.d("TAG", "onViewCreated: $loginResult")
                if (loginResult.accessToken != "") {
                    MainActivity.JWT.value = loginResult.accessToken
                    MainActivity.email = loginResult.loggedInUser?.email ?: ""
                    MainActivity.userName = loginResult.loggedInUser?.displayName ?: ""
                    MainActivity.connection.start()
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMobileNavigation())
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(com.example.chat.R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}