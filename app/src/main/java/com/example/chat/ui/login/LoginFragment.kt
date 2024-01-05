package com.example.chat.ui.login

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.chat.auth.FacebookAuthentication
import com.example.chat.auth.GoogleAuthentication
import com.example.chat.databinding.FragmentLoginBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        isUserSignedIn()
        val googleAuth =
            GoogleAuthentication(
                registerForActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val task: Task<GoogleSignInAccount> =
                            GoogleSignIn.getSignedInAccountFromIntent(
                                result.data
                            )
                        handleGoogleAuthResult(task)
                    }
                },
                context = requireActivity()
            )
        binding.oAuthContainerLayout.btnGoogleOAuth.setOnClickListener {
            googleAuth.authenticate()
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
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

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
                .navigate(com.example.chat.R.id.action_loginFragment_to_signupFragment)
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
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
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