package com.ssafy.witch.data.model.dto

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("userId")val userId: String,
    @SerializedName("email")val email: String,
    @SerializedName("nickname")val nickname: String,
    @SerializedName("profileImageUrl")val profileImageUrl: String,
    @SerializedName("isLeader") val isLeader: Boolean
){
    constructor():this("", "", "","", false)
    constructor(nickname: String):this("", "", nickname, "", false)
    constructor(userId: String, email: String, nickname: String, profileImageUrl: String):this(userId, email, nickname, profileImageUrl, false)
    constructor(userId: String, nickname: String,profileImageUrl: String):this(userId, "", nickname, profileImageUrl, false)
}