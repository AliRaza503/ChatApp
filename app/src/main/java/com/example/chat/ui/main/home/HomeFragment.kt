package com.example.chat.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.MainActivity
import com.example.chat.adapters.HomeFragmentAdapter
import com.example.chat.databinding.FragmentHomeBinding
import com.example.chat.ui.main.dialogs.GetEmailDialog

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }
    private val adapter: HomeFragmentAdapter by lazy {
        HomeFragmentAdapter()
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Set the adapter
        binding.contactsRv.adapter = this@HomeFragment.adapter
        binding.contactsRv.layoutManager = LinearLayoutManager(requireContext())

        MainActivity.emails.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        homeViewModel.findEmailResult.observe(viewLifecycleOwner) {
            if (it != null && it) {
                //TODO: WHY IS EMAIL NULL HERE?
                // Navigate to Chat Fragment
                findNavController().navigate(
                    HomeFragmentDirections.actionNavigationHomeToMessagesFragment(
                        email = homeViewModel.email
                    )
                )
            }
        }

        binding.newChatFab.setOnClickListener {
            // show dialog box to enter email of user to chat with
            showDialogBox()
        }

        binding.toolbarTv.text = MainActivity.userName

        return binding.root
    }

    private fun showDialogBox() {
        GetEmailDialog(requireContext()) { email ->
            homeViewModel.findEmail(email)
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}