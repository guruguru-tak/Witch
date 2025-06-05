package com.ssafy.witch.data.remote

import com.ssafy.witch.base.ApplicationClass

class RetrofitUtil {
    companion object{
        val userService = ApplicationClass.retrofit.create(UserService::class.java)

        val appointmentService = ApplicationClass.retrofit.create(AppointmentService::class.java)

        val authService = ApplicationClass.retrofitLogin.create(AuthService::class.java)

        val tokenService = ApplicationClass.retrofitLogin.create(TokenService::class.java)

        val groupService = ApplicationClass.retrofit.create(GroupService::class.java)

        val s3Service = ApplicationClass.retrofit.create(S3Service::class.java)

        val snackService = ApplicationClass.retrofit.create(SnackService::class.java)
    }

}