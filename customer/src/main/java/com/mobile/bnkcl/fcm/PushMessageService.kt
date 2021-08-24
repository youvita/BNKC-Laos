package com.mobile.bnkcl.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushMessageService : FirebaseMessagingService() {

    companion object {
        const val NOTIFICATION_ID = 1002
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            Log.d("nng: >>>", "fcm: ${remoteMessage.notification?.title}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

}