package com.example.chat.ui.main.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.chat.MainActivity
import com.example.chat.R
import com.example.chat.adapters.MessagesFragmentAdapter
import com.example.chat.databinding.FragmentMessagesBinding

class MessagesFragment : Fragment() {

    private val viewModel: MessagesViewModel by lazy {
        MessagesViewModel()
    }

    // Get Arguments
    private val args: MessagesFragmentArgs by lazy {
        MessagesFragmentArgs.fromBundle(requireArguments())
    }

    private val binding: FragmentMessagesBinding by lazy {
        FragmentMessagesBinding.inflate(layoutInflater)
    }

    private val adapter: MessagesFragmentAdapter by lazy {
        MessagesFragmentAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Set the email of the user to chat with
        viewModel.receiverEmail = args.email
        binding.toolbarTv.text = args.email

        // Set the adapter
        binding.messagesRv.adapter = adapter

        // Loading all the messages which are sent by [viewModel.receiverEmail] and received by [MainActivity.email]
        MainActivity.emails.value?.find { email -> email.email == viewModel.receiverEmail }?.messages?.let {
            it.value?.let { message -> viewModel.addToList(message, viewModel.receiverEmail) }
        }

        // TODO: Loading all the messages which are sent by [MainActivity.email] and received by [viewModel.receiverEmail]
        MainActivity.emails.value?.find { email -> email.email == MainActivity.email }?.messages?.let {
            it.value?.let { message -> viewModel.addToList(message, MainActivity.email) }
        }

        adapter.submitList(viewModel.messages)

        binding.navigateUp.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.sendMessageBtn.setOnClickListener {
            val message = binding.messageEt.text.toString()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message) {
                    // TODO: Add to the messages in the main activity
                    viewModel.messages.add(
                        MessagesFragmentAdapter.MessageItem(
                            message,
                            MainActivity.email
                        )
                    )
                    adapter.submitList(viewModel.messages)
                    binding.messageEt.text?.clear()
                }
            }
        }

        return binding.root
    }
}