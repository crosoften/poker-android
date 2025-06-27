package com.draccoapp.poker.utils

import android.content.Context
import com.draccoapp.poker.api.service.NotificationService
import com.draccoapp.poker.repository.NotificationRepository
import kotlinx.coroutines.Dispatchers

class NotificationHelper(private val context: Context) {

    private val preferences by lazy { Preferences(context) }

    fun createNotificationRepository(notificationService: NotificationService): NotificationRepository {
        return NotificationRepository(notificationService, preferences, Dispatchers.IO)
    }

    fun createFirebaseTokenManager(notificationRepository: NotificationRepository): FirebaseTokenManager {
        return FirebaseTokenManager(context, notificationRepository)
    }
}
