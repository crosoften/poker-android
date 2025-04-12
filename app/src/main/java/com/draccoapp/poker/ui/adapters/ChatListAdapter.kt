package com.draccoapp.poker.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.draccoapp.poker.api.model.response.chat.ChatResponse
import com.draccoapp.poker.databinding.ItemRvChatBinding
import java.util.Locale

class ChatListAdapter(
    private val chats: List<ChatResponse>,
    private val onChatClickListener: (ChatResponse) -> Unit
) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRvChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatItem: ChatResponse){
            binding.apply {
                chatUserName.text = chatItem.title
                if(chatItem.messages.isNotEmpty()) chatLastMessage.text = chatItem.messages[0].content
                else chatLastMessage.visibility = View.GONE

                if(chatItem.unreadMessages == 0) chatQuantityMessageContainer.visibility = View.GONE
                else chatQuantityMessage.text = String.format(Locale.getDefault(), "%d", chatItem.unreadMessages)

                root.setOnClickListener { onChatClickListener(chatItem) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRvChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = chats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chats[position]
        holder.bind(chat)
    }
}