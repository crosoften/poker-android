package com.draccoapp.poker.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.draccoapp.poker.api.model.response.chat.ChatResponse
import com.draccoapp.poker.repository.CoachRepository
import kotlinx.coroutines.launch

class CoachViewModel(private val repository: CoachRepository) : ViewModel() {

    val chats = MutableLiveData<List<ChatResponse>>()
    val chatMessages = MutableLiveData<Unit>()
    val error = MutableLiveData<String>()

    init {
        loadChats()
    }

    private fun loadChats() {
        try {
            viewModelScope.launch {
                chats.postValue(repository.getAllChats())
            }
        } catch (e: Exception) {
            error.postValue(e.message)
        }
    }

    fun getChatById(chatId: String){
        try {
            viewModelScope.launch {
                chatMessages.postValue(repository.getChatById(chatId))
            }
        } catch (e: Exception) {
            error.postValue(e.message)
        }
    }
}