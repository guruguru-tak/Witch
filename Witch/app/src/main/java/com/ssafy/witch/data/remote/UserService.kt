package com.ssafy.witch.data.remote

import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.EditPwd
import com.ssafy.witch.data.model.dto.Login
import com.ssafy.witch.data.model.dto.ObjectKey
import com.ssafy.witch.data.model.dto.User
import com.ssafy.witch.data.model.response.PresignedUrl
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {

    @GET("users/me")
    suspend fun getProfile(

    ) : Response<BaseResponse<User>>

    @PATCH("users/me/nickname")
    suspend fun editProfileName(
        @Body name: String
    ) : Response<BaseResponse<String>>


    @POST("users/me/profile-image/presigned-url")
    suspend fun getPresignedUrl(
        @Body filename: String
    ) : Response<BaseResponse<PresignedUrl>>


    @PATCH("users/me/profile-image")
    suspend fun editProfileImage(
        @Body objectKey: ObjectKey
    ) : Response<BaseResponse<String>>


    @GET("users/nickname/is-unique")
    suspend fun checkNicknameUnique(
        @Query("nickname") nickname: String
    ): Response<BaseResponse<String>>

    @PATCH("users/me/password")
    suspend fun editPassword(
        @Body editPassword: EditPwd
    ) : Response<BaseResponse<String>>
}
