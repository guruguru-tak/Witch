package com.ssafy.witch.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ssafy.witch.R

enum class appointmentFcm{ // APPOINTMENT_DELETED
    APPOINTMENT_START, APPOINTMENT_END
    , APPOINTMENT_JOIN, APPOINTMENT_EXIT, APPOINTMENT_ARRIVAL
}

enum class groupFcm { // GROUP_DELETED
    GROUP_JOIN_REQUEST, GROUP_JOIN_REQUEST_APPROVE
    , GROUP_JOIN_REQUEST_REJECT, APPOINTMENT_CREATED
}

private const val TAG = "ForegroundService_Witch"
class ForegroundService : Service() {

    private lateinit var fcmType: String

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val type = intent?.getStringExtra("type") ?: "default"
        val parameter = intent?.getStringExtra("parameter") ?: "파라미터"
        val title = intent?.getStringExtra("title") ?: "알림 제목"
        val content = intent?.getStringExtra("body") ?: "알림 내용"

        fcmType = getFcmType(type)
        val channelId = getChannelId(fcmType)
        createNotificationChannel(channelId, fcmType)
        startForegroundService(channelId, fcmType, parameter, title, content)

        return START_STICKY
    }

    private fun getFcmType(type: String): String {
        return when {
            groupFcm.values().map { it.name }.contains(type) -> "group"
            appointmentFcm.values().map { it.name }.contains(type) -> "appointment"
            else -> "basic"
        }
    }

    private fun startForegroundService(channelId: String, fcmType: String, parameter: String, title: String, content: String) {
        val pendingIntent = createPendingIntent(fcmType, parameter)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.app_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(101, notification)
    }

    private fun createNotificationChannel(channelId: String, type: String) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.createNotificationChannel(NotificationChannel(channelId, getChannelName(type), importance))
    }

    private fun getChannelId(fcmType: String): String {
        return when (fcmType) {
            "appointment" -> "appointment_channel"
            "group" -> "group_channel"
            else -> "default_channel"
        }
    }

    private fun getChannelName(fcmType: String): String {
        return when (fcmType) {
            "appointment" -> "약속 알림"
            "group" -> "모임 알림"
            else -> "기본 알림"
        }
    }

    private fun createPendingIntent(fcmType: String, parameter: String): PendingIntent {
        val intent = when (fcmType) {
            "group" -> Intent(this, MainActivity::class.java).apply {
                putExtra("openFragment", 5)
                putExtra("id", parameter)
            }
            "appointment" -> Intent(this, ContentActivity::class.java).apply {
                putExtra("openFragment", 9)
                putExtra("id", parameter)
            }
            else -> Intent(this, LoginActivity::class.java).apply {
                putExtra("openFragment", 1)
            }
        }

        return PendingIntent.getActivity(this, 10101, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

}