package com.ssafy.witch.data.model.dto

data class Snack(
    val snackId: String,
    val user: User,
    val createdAt : String,
    val snackImageUrl: String,
    val snackSoundUrl: String,
    val latitude: Double,
    val longitude: Double,
    val snackImageObjectKey: String?,
    val snackSoundObjectKey: String?,
    val appointmentId: String?
){
    constructor(latitude: Double, longitude: Double, snackImageObjectKey: String?, snackSoundObjectKey: String?):this("", User(), "", "", "", latitude, longitude, snackImageObjectKey, snackSoundObjectKey,"")
    constructor(nickname: String,latitude: Double, longitude: Double, snackImageUrl: String, snackSoundUrl: String): this("", User(nickname), "", snackImageUrl, snackSoundUrl, latitude, longitude, "", "","")
    constructor() : this("", User(), "", "", "", 0.0, 0.0, "", "","")
}
