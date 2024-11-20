package com.draccoapp.poker.api.model.response.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.ChatMessageReceived

class MessageAdapter(private val messages: List<ChatMessageReceived>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val SENT_MESSAGE = 0
        private const val RECEIVED_MESSAGE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sentByPlayer) SENT_MESSAGE else RECEIVED_MESSAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SENT_MESSAGE -> MessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message_sent, parent, false))
            RECEIVED_MESSAGE -> ReceivedMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message_received, parent, false))
            else -> throw RuntimeException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is MessageViewHolder -> holder.messageTextView.text = message.content
            is ReceivedMessageViewHolder -> holder.messageTextView.text = message.content
        }
    }

    override fun getItemCount(): Int = messages.size
}

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val messageTextView: TextView = itemView.findViewById(R.id.message_content)
}

class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val messageTextView: TextView = itemView.findViewById(R.id.message_content)
}