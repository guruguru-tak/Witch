package com.ssafy.witch.data.model.dto

import java.time.LocalDateTime

data class MyAppointment(
    val appointmentId: String,
    val appointmentTime: String,
    val name: String,
    val status: String,
    val isMyAppointment: Boolean,
    val group: Group,
){
    data class Group(
        val groupId: String,
        val groupImageUrl: String,
        val name: String
    )

    constructor():this("", "", "", "", false, Group("","" ,""))
    constructor(appointmentId: String, appointmentTime: String, name: String, status: String, isMyAppointment: Boolean):this(appointmentId, appointmentTime, name, status, isMyAppointment, Group("","",""))
}