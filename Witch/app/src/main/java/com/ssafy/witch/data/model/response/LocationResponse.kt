package com.ssafy.witch.data.model.response

import com.google.gson.annotations.SerializedName

data class LocationResponse (
    @SerializedName("positions") var positions: List<LocationInfo>
){
    data class LocationInfo(
        val userId: String,
        val nickname: String,
        val profileImageUrl: String,
        val latitude: Double,
        val longitude: Double
    ){
        constructor(latitude: Double, longitude: Double) : this("", "", "", latitude, longitude)
    }
}