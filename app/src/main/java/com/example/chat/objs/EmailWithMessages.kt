package com.example.chat.objs

import androidx.lifecycle.MutableLiveData

data class EmailWithMessages(
    var email: String,
    val messages: MutableLiveData<MutableList<String>>
)