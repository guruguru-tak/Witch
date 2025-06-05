package com.ssafy.witch.data.model.dto

data class Snacks(
    val snackList : List<SnackItem>
){
    data class SnackItem(
        val snackId: String,
        val userName: String,
        val createdAt : String,
        val snackImageUrl: String,
        val snackAudioUrl: String,
        val snackLatitute: Double,
        val snackLongitute: Double,
    ){
        constructor() : this("", "", "", "", "", 0.0, 0.0)
    }
}