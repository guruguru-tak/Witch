package com.ssafy.witch.data.remote

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.ssafy.witch.ui.appointment.MyLocationForegroundService

private const val TAG = "LocationReceiver_Witch"
class LocationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive: ")
        val flag = intent.getStringExtra("flag") ?: ""
        val appointmentId = intent.getStringExtra("appointmentId") ?: ""

        if (flag == "cancel") {
            Log.d(TAG, "onReceive: 알람 취소 요청 감지됨")
            stopForegroundService(context)
            return
        }

        Log.d(TAG, "onReceive: 위치 서비스 시작")
        val serviceIntent = Intent(context, MyLocationForegroundService::class.java).apply {
            putExtra("appointmentId", appointmentId)
        }
        context.startForegroundService(serviceIntent)
    }

    private fun stopForegroundService(context: Context) {
        val stopIntent = Intent(context, MyLocationForegroundService::class.java)
        context.stopService(stopIntent)
    }

}