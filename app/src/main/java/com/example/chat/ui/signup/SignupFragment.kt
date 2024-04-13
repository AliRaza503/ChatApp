package com.example.chat.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chat.MainActivity
import com.example.chat.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {

    private val viewModel: SignupViewModel by lazy {
        SignupViewModel()
    }
    private val binding: FragmentSignupBinding by lazy {
        FragmentSignupBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.btnSignup.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            viewModel.signup(
                binding.nameET.text.toString(),
                binding.emailET.text.toString(),
                binding.passwordET.text.toString(),
                binding.confPasswordET.text.toString()
            )
        }
        viewModel.signupResult.observe(viewLifecycleOwner) { result ->
            binding.loading.visibility = View.GONE
            // TODO: save the response to shared preferences
            if (result.accessToken != "") {
                MainActivity.JWT.value = result.accessToken
                MainActivity.email = result.loggedInUser?.email ?: ""
                MainActivity.userName = result.loggedInUser?.displayName ?: ""
                MainActivity.connection.start()
                findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToMobileNavigation())
            }
        }
        binding.navigateUp.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
        }
        return binding.root
    }
}