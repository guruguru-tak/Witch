package com.ssafy.witch.data.model.response

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    @SerializedName("tokenType") val tokenType: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("accessTokenExpiresIn") val accessTokenExpiresIn: Long,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("refreshTokenExpiresIn") val refreshTokenExpiresIn: Long,
    @SerializedName("refreshTokenRenewAvailableSeconds") val refreshTokenRenewAvailableSeconds: Long
)