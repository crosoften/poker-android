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
        Log.i("socketTest", "connect: ${preferences.getToken()}")
//        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2NzM2MTAwYjJjOTdjMzlhMzAyNWJiOTAiLCJyb2xlIjoicGxheWVyIiwiaWF0IjoxNzMxOTM1OTEzfQ.iB_QjkRASf4tjZoucoxlNqOcAFycl_phzh0mv8PZqBc"
        options.extraHeaders = mapOf("Authorization" to listOf(preferences.getToken()))

        socket = IO.socket("https://propath.standardbacking.com:8080", options)
        socket?.connect()

        // Eventos de conexão, desconexão e recebimento de mensagens
        socket?.on(Socket.EVENT_CONNECT) {
            Log.i("socketTest", "connect: conectado")
            socket?.emit(event, "{\"chatId\":\"67373908ea3afdcf0c9e78fc\", \"content\":\"123456aa\"}")
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
        val gson = Gson()
        val jsonMessage = gson.toJson(message)
        Log.e("socketTest", "sendMessage: $jsonMessage")
        socket?.emit(event, "{\"chatId\":\"67373908ea3afdcf0c9e78fc\", \"content\":\"123456\"}")
    }

    fun disconnect() {
        socket?.disconnect()
    }
}