package com.ssafy.witch.util

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


private const val TAG = "TimeConverter"
class TimeConverter {
    @RequiresApi(Build.VERSION_CODES.O)
    fun calDaysDiff(time: String): Int {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val givenDate = LocalDateTime.parse(time, formatter).toLocalDate()
        val currentDate = LocalDate.now()

        Log.d(TAG, "calDaysDiff: $givenDate")
        Log.d(TAG, "calDaysDiff:  $currentDate")
        
        return ChronoUnit.DAYS.between(givenDate, currentDate).toInt() +1
    }


    @SuppressLint("NewApi")
    fun convertToLocalDateTime(time: String): LocalDateTime {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        return LocalDateTime.parse(time, formatter)
    }
}