package com.ssafy.witch.data.model.dto

data class Join(
    val email: String,
    val password: String,
    val nickname: String,
    val emailVerificationCode: String
)
