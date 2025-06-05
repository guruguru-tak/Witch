package com.ssafy.witch.data.remote

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ssafy.witch.ui.ForegroundService

class MyFirebaseMessageService :  FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        var type = message.data["type"].toString()
        var parameter = message.data["parameter"].toString()
        var title = message.data["title"].toString()
        var content = message.data["content"].toString()

        if (title != "null") {
            sendToForegroundService(type, parameter, title, content)
        }
    }

    private fun sendToForegroundService(type: String, parameter: String, title: String, content: String) {
        val intent = Intent(this, ForegroundService::class.java).apply {
            putExtra("type", type)
            putExtra("parameter", parameter)
            putExtra("title", title)
            putExtra("content", content)
        }
        startForegroundService(intent)
    }

}