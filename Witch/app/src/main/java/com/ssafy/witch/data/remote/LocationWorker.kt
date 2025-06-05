package com.ssafy.witch.data.remote

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.MyAppointment
import com.ssafy.witch.data.model.response.ErrorResponse
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.appointmentService
import java.time.Duration
import java.time.LocalDateTime

private const val TAG = "LocationWorker_Witch"
class LocationWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {

    @SuppressLint("MissingPermission")
    suspend override fun doWork(): Result {
        Log.d(TAG, "LocationWorker started")

        // 실시간 위치를 보내기 위한 약속 시간 확인
        runCatching {
            val thisMonthAppointments = appointmentService.getNextAppointments(LocalDateTime.now().year, LocalDateTime.now().monthValue)
            val nextMonthAppointments = appointmentService.getNextAppointments(LocalDateTime.now().year, LocalDateTime.now().monthValue+1)

            val appointments = mutableListOf<MyAppointment>().apply {
                this.addAll(thisMonthAppointments.body()?.data?.appointments ?: emptyList())
                this.addAll(nextMonthAppointments.body()?.data?.appointments ?: emptyList())
            }

            for(appointment in appointments){
                if (appointment.status == "ONGOING") {
                    Log.d(TAG, "doWork(): ${appointment.appointmentId}")
                    val endTime = Duration.between(LocalDateTime.now(), LocalDateTime.parse(appointment.appointmentTime))
                    scheduleAlarm(applicationContext, 0, endTime.seconds, appointment.appointmentId)
                    return Result.success()
                }
            }
        }.onSuccess { response ->
            val thisMonthAppointments = appointmentService.getNextAppointments(LocalDateTime.now().year, LocalDateTime.now().monthValue)
            val nextMonthAppointments = appointmentService.getNextAppointments(LocalDateTime.now().year, LocalDateTime.now().monthValue + 1)

            if (thisMonthAppointments.body()?.success == false) {
                Log.d(TAG, "doWork(): this month appointments: ${thisMonthAppointments.body()?.error?.errorMessage}")
            }
            if (nextMonthAppointments.body()?.success == false) {
                Log.d(TAG, "doWork(): next month appointments: ${nextMonthAppointments.body()?.error?.errorMessage}")
            }
        }.onFailure {
            Log.d(TAG, "doWork(): ${it.message}")
        }

        return Result.success()
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleAlarm(context: Context, startTime: Long, endTime: Long, appointmentId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, LocationReceiver::class.java).apply {
            putExtra("appointmentId", appointmentId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, 201, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = System.currentTimeMillis() + (startTime * 60 * 1000)

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)

        scheduleCancelAlarm(context, endTime, appointmentId)
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleCancelAlarm(context: Context, cancelDelay: Long, appointmentId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, LocationReceiver::class.java).apply {
            putExtra("flag", "cancel")
            putExtra("appointmentId", appointmentId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, 202, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val cancelTriggerTime = System.currentTimeMillis() + (cancelDelay * 60 * 1000)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cancelTriggerTime, pendingIntent)
    }

}