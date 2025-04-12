package com.draccoapp.poker.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.draccoapp.poker.api.model.ChatMessageReceived
import com.draccoapp.poker.api.model.ChatMessageSend
import com.draccoapp.poker.api.model.response.chat.ChatResponse
import com.draccoapp.poker.api.service.chatSocket.ChatSocketService
import com.draccoapp.poker.repository.CoachRepository
import kotlinx.coroutines.launch

class CoachViewModel(
    private val repository: CoachRepository,
    private val socket: ChatSocketService
) : ViewModel() {
    val chats = MutableLiveData<List<ChatResponse>>()
    val chatInformation = MutableLiveData<ChatResponse>()
    val messages = MutableLiveData<List<ChatMessageReceived>>()
    val error = MutableLiveData<String>()

    init {
        loadChats()
    }

    fun connect() {
        socket.connect()
    }

    fun disconnect() {
        socket.disconnect()
    }

    fun sendMessage(message: String, chatId: String) {
        val convertedMessage = ChatMessageSend(
            chatId = chatId,
            content = message
        )
        socket.sendMessage(convertedMessage)
    }

    fun readMessages() {
        socket.onMessageReceived { message ->
            var oldMessages = messages.value
            if (oldMessages == null) {
                oldMessages = listOf()
            }
            messages.postValue(oldMessages + message)
        }
    }

    fun loadChats() {
        try {
            viewModelScope.launch {
                chats.postValue(repository.getAllChats())
            }
        } catch (e: Exception) {
            error.postValue(e.message)
        }
    }

    fun getChatById(chatId: String) {
        try {
            viewModelScope.launch {
                val response = repository.getChatById(chatId)
                chatInformation.value = response
                messages.postValue(response.messages)
            }
        } catch (e: Exception) {
            error.postValue(e.message)
        }
    }
}