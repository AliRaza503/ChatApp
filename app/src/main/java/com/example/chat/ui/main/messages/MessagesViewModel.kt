package com.example.chat.ui.main.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.MainActivity
import com.example.chat.adapters.MessagesFragmentAdapter
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.launch

class MessagesViewModel : ViewModel() {
    val messages = mutableListOf<MessagesFragmentAdapter.MessageItem>()
    fun addToList(value: MutableList<String>, receiverEmail: String) {
        viewModelScope.launch {
            value.forEach {
                messages.add(MessagesFragmentAdapter.MessageItem(it, receiverEmail))
            }
        }
    }

    fun sendMessage(message: String, messageSent: () -> Unit) {
        openConnection()
        viewModelScope.launch {
            MainActivity.connection.send(
                "sendMessage",
                mapOf(
                    "email" to receiverEmail,
                    "senderEmail" to MainActivity.email,
                    "message" to message
                )
            ).apply {
                messageSent()
            }
        }
    }

    private fun openConnection() {
        if (MainActivity.connection.connectionState != HubConnectionState.CONNECTED && MainActivity.connection.connectionState != HubConnectionState.CONNECTING) {

            MainActivity.connection.start().blockingAwait()
        }
    }

    lateinit var receiverEmail: String
}