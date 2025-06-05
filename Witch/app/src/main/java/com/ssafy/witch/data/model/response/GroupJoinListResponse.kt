package com.ssafy.witch.data.model.response

import com.google.gson.annotations.SerializedName
import com.ssafy.witch.data.model.dto.MyAppointment
import com.ssafy.witch.data.model.dto.User
import java.time.LocalDateTime

data class GroupJoinListResponse(
    @SerializedName("joinRequests") val joinRequests: List<JoinRequest>
){
    data class JoinRequest(
        @SerializedName("joinRequestId") val joinRequestId: String,
        @SerializedName("user") val user: User,
    )
}
