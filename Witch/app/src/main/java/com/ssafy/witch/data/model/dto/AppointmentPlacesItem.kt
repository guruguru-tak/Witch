package com.ssafy.witch.data.model.dto

import java.math.BigDecimal

data class AppointmentPlacesItem(
    val placeName: String,
    val placeAddress: String,
    val placeLatitude: BigDecimal,
    val placeLongitude: BigDecimal,
    val placePhoneNumber: String
)