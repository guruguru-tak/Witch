package com.ssafy.witch.data.model.response

import com.google.gson.annotations.SerializedName

data class SnackResponse(
    @SerializedName("snacks") var snacks: List<SnackInfo>
){
    data class SnackInfo(
        val snackId: String,
        val longitude: Double,
        val latitude: Double,
        val snackImageUrl: String,
        val snackSoundUrl: String,
        val createdAt: String,
        @SerializedName("user") var user: UserInfo
    ) {
        data class UserInfo(
            val userId: String,
            val nickname: String,
            val profileImageUrl: String
        )
    }
}
