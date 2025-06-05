package com.ssafy.witch.data.model.response

import com.google.gson.annotations.SerializedName
import com.ssafy.witch.data.model.dto.MyAppointment
import com.ssafy.witch.data.model.dto.User
import java.time.LocalDateTime

data class GroupMemberResponse(
    @SerializedName("members") val members: List<User>
)
