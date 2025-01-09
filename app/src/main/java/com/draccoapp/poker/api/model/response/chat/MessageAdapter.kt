package com.draccoapp.poker.api.model.response.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.ChatMessageReceived
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MessageAdapter(private val messages: List<ChatMessageReceived>, private val userId: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val SENT_MESSAGE = 0
        private const val RECEIVED_MESSAGE = 1
    }

    override fun getItemViewType(position: Int): Int {
        Log.i("isSentByPlayer", "getItemViewType: ${userId == messages[position].sentById}")
        Log.i("isSentByPlayer", "userId: ${userId}")
        Log.i("isSentByPlayer", "messageId: ${messages[position].sentById}")
        return if(userId == messages[position].sentById) SENT_MESSAGE else RECEIVED_MESSAGE
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
            is MessageViewHolder -> {
                holder.messageTextView.text = message.content
                holder.time.text = formatDateTimeToHHMM(message.createdAt)

            }
            is ReceivedMessageViewHolder -> {
                holder.messageTextView.text = message.content
                holder.time.text = formatDateTimeToHHMM(message.createdAt)
            }
        }
    }

    override fun getItemCount(): Int = messages.size
}

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val messageTextView: TextView = itemView.findViewById(R.id.message_content)
    val time: TextView = itemView.findViewById(R.id.message_time)
}

class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val messageTextView: TextView = itemView.findViewById(R.id.message_content)
    val time: TextView = itemView.findViewById(R.id.message_time)
}

fun formatDateTimeToHHMM(dateTimeString: String): String {
    val instant = Instant.parse(dateTimeString)
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
        .withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}