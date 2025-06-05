package com.ssafy.witch.data.model.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.time.LocalDateTime

data class AppointmentDetailItem(
    var appointmentId: String,
    var name: String,
    var appointmentStatus: String,
    var summary: String,
    var appointmentTime: String,
    var address: String,
    var latitude: Double,
    var longitude: Double,
    @SerializedName("members") var participants: List<Participants>
) {
    data class Participants (
        var userId: String,
        var nickname:String,
        var profileImageUrl:String,
        var isLeader: Boolean,
//        var is_late: Boolean
    )
}