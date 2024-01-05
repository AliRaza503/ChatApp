package com.example.chat.data.model

import androidx.recyclerview.widget.DiffUtil

class ContactDiffUtils: DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return newItem.email == oldItem.email
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return newItem.displayName == oldItem.displayName
    }
}