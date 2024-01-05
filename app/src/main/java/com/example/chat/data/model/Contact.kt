package com.example.chat.data.model

import android.provider.ContactsContract.CommonDataKinds.Email

data class Contact(
    val email: Email,
    val displayName: String,
)
