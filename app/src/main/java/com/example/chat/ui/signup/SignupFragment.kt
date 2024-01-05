package com.example.chat.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chat.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {

    companion object {
        fun newInstance() = SignupFragment()
    }

    private lateinit var viewModel: SignupViewModel
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater)
        binding.btnSignup.setOnClickListener {
            checkCompleted()
        }
        binding.navigateUp.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    /**
     * If all the text fields are filled make API call to register the user
     */
    private fun checkCompleted() {
        if (binding.emailET.text.toString().isNotEmpty() &&
            binding.passwordET.text.toString().isNotEmpty() &&
            binding.nameET.text.toString().isNotEmpty() &&
            binding.confPasswordET.text.toString().isNotEmpty()
        ) {
            if (binding.passwordET.text.toString() == binding.confPasswordET.text.toString()) {
                // Make API call to register the user
            }
        }
    }


}