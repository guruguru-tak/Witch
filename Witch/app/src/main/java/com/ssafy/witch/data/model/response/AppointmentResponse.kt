package com.ssafy.witch.data.model.response

import com.ssafy.witch.data.model.dto.MyAppointment

data class AppointmentResponse(
    var appointmentId: String,
    var name: String,
    var status: String,
    var appointmentTime: String,
    var group: MyAppointment.Group
)
