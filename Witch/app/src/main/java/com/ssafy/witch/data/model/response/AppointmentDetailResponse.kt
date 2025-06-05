package com.ssafy.witch.data.model.response

import com.google.gson.annotations.SerializedName
import com.ssafy.witch.data.model.dto.AppointmentDetailItem

data class AppointmentDetailResponse(
    @SerializedName("appointments") val appointments: List<AppointmentDetailItem>
)