package com.ssafy.witch.data.model.response

import com.google.gson.annotations.SerializedName
import com.ssafy.witch.data.model.dto.MyAppointment
import java.time.LocalDateTime

data class MyAppointmentResponse(
    @SerializedName("appointments") val appointments: List<MyAppointment>
)
