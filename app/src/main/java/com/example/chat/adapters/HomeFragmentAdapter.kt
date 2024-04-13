package com.example.chat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.R
import com.example.chat.objs.EmailWithMessages

class HomeFragmentAdapter :
    ListAdapter<EmailWithMessages, HomeFragmentAdapter.ViewHolder>(EmailWithMessagesDiffCallback()) {

    // ViewHolder for the list items
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val email: TextView = view.findViewById(R.id.username_tv)
        private val message: TextView = view.findViewById(R.id.message_tv)

        fun bind(item: EmailWithMessages) {
            email.text = item.email
            message.text = item.messages.value?.lastOrNull()
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class EmailWithMessagesDiffCallback : DiffUtil.ItemCallback<EmailWithMessages>() {
    override fun areItemsTheSame(oldItem: EmailWithMessages, newItem: EmailWithMessages): Boolean {
        return oldItem.email == newItem.email
    }

    override fun areContentsTheSame(oldItem: EmailWithMessages, newItem: EmailWithMessages): Boolean {
        return oldItem == newItem
    }
}
