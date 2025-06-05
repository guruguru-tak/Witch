package com.ssafy.witch.data.model.dto.request

data class AppointmentRequest (
    val name: String,
    val summary: String,
    val appointmentTime: String,
    val latitude: Double,
    val longitude: Double,
    val address: String
)