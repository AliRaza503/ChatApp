package com.example.chat.OnBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.chat.R
import com.example.chat.databinding.FragmentOnBoardingBinding


class OnBoarding : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root

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