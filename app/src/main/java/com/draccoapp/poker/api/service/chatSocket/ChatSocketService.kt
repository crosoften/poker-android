package com.draccoapp.poker.api.service.chatSocket
import android.util.Log
import com.draccoapp.poker.api.model.ChatMessageReceived
import com.draccoapp.poker.api.model.ChatMessageSend
import com.draccoapp.poker.utils.Preferences
import com.google.gson.Gson

import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class ChatSocketService(
    private val preferences: Preferences
) {
    private var socket: Socket? = null
    private val event = "message"

    fun connect() {
        val options = IO.Options()
        options.extraHeaders = mapOf("Authorization" to listOf(preferences.getToken()))

        socket = IO.socket("https://propath.standardbacking.com:8080", options)
        socket?.connect()

        socket?.on(Socket.EVENT_CONNECT) {
            Log.i("socketTest", "connect: conectado")
        }
        socket?.on(Socket.EVENT_DISCONNECT) {
            Log.i("socketTest", "desconectado")
        }
        socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
            Log.e("socketTest", "Erro ao conectar: ${args[0]}")
        }
    }

    fun onMessageReceived(
        listener: (ChatMessageReceived) -> Unit
    ){
        socket?.on(event){ args ->
            if (args[0] is JSONObject) {
                val jsonMessage = (args[0] as JSONObject).toString()
                Log.i("socketTest", "onMessageReceived - JSON: $jsonMessage")

                val messageConverted = Gson().fromJson(jsonMessage, ChatMessageReceived::class.java)
                Log.i("socketTest", "onMessageReceived - converted: $messageConverted")

                listener(messageConverted)
            } else {
                Log.e("socketTest", "Erro: Esperado um JSONObject, mas obtido: ${args[0]::class.java}")
            }
        }
    }

    fun sendMessage(message: ChatMessageSend) {
        val jsonMessage = JSONObject("{\"chatId\":\"${message.chatId}\", \"content\":\"${message.content}\"}")
        socket?.emit(event, jsonMessage)
    }

    fun disconnect() {
        socket?.disconnect()
    }
}