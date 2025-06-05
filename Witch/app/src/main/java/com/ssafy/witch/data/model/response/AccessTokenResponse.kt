package com.ssafy.witch.data.model.response

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
    @SerializedName("tokenType") val tokenType: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("accessTokenExpiresIn") val accessTokenExpiresIn: Long
)
