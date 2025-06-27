package com.draccoapp.poker.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.draccoapp.poker.api.model.request.TokenRequest
import com.draccoapp.poker.api.service.NotificationService
import com.draccoapp.poker.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    // Inicializar dependências manualmente
    private val preferences by lazy { Preferences(this) }
    private val notificationService by lazy { createNotificationService() }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")

        // Enviar token para o servidor
        sendTokenToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "From: ${remoteMessage.from}")

        // Verificar se a mensagem contém dados
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            handleDataMessage(remoteMessage.data)
        }

        // Verificar se a mensagem contém notificação
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            showNotification(it.title, it.body)
        }
    }

    private fun sendTokenToServer(token: String) {
        serviceScope.launch {
            try {
                val accountId = getCurrentAccountId()
                val request = TokenRequest(accountId = accountId, token = token)
                val response = notificationService.saveToken(request)

                if (response.isSuccessful) {
                    Log.d(TAG, "Token saved successfully")
                } else {
                    Log.e(TAG, "Failed to save token: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error saving token", e)
            }
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val title = data["title"]
        val body = data["body"]
        val type = data["type"]

        when (type) {
            "tournament_update" -> {
                showNotification(title, body)
            }
            "new_tournament" -> {
                showNotification(title, body)
            }
            else -> {
                showNotification(title, body)
            }
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Criar canal de notificação para Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Poker Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Intent para abrir o app quando a notificação for clicada
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title ?: "Poker App")
            .setContentText(body ?: "Nova notificação")
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Usando ícone padrão do sistema
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationService(): NotificationService {
        // Ajuste a URL base conforme sua API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://sua-api.com/") // Substitua pela sua URL
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(NotificationService::class.java)
    }

    private fun getCurrentAccountId(): String {
        // Ajuste conforme sua implementação de Preferences
        return preferences.getUserId().toString() // ou como você obtém o accountId
    }

    companion object {
        private const val TAG = "FCMService"
        private const val CHANNEL_ID = "poker_notifications"
        private const val NOTIFICATION_ID = 1
    }
}
