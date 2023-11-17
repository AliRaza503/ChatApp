package com.example.chat.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chat.R
import com.example.chat.databinding.FragmentSignupBinding
import okhttp3.OkHttp

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
                // Make API call to register a user
                val api = RetrofitClient.apiService

                val name = binding.nameET.text.toString()
                val email = binding.emailET.text.toString()
                val password = binding.passwordET.text.toString()

                val call = api.registerUser(name, email, password)

                call.enqueue(object : Callback<RegistrationResponse> {
                    override fun onResponse(
                        call: Call<RegistrationResponse>,
                        response: Response<RegistrationResponse>
                    ) {
                        if (response.isSuccessful) {
                            // Registration successful, navigate to the login fragment
                            // You can replace the following code with your navigation logic
                            // Show a toast message
                            Toast.makeText(
                                context,
                                "Registered ${response.body()?.user?.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                        } else {
                            // Handle unsuccessful registration (e.g., show an error message)
                        }
                    }

                    override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                        // Handle failure (e.g., show an error message)
                    }
                })
            }
        }
    }


}
