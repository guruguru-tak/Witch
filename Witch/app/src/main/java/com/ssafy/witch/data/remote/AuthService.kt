package com.ssafy.witch.data.remote

import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.Join
import com.ssafy.witch.data.model.dto.Login
import com.ssafy.witch.data.model.dto.RefreshToken
import com.ssafy.witch.data.model.response.AccessTokenResponse
import com.ssafy.witch.data.model.response.LoginResponse
import com.ssafy.witch.data.model.response.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    // 로그인 API
    //로그인 처리 후 성공적으로 로그인 되었다면 JWT 토큰 발급 API
    //서버에서 정확히 “Content-Type: application/json”만을 요구, 메서드 단위의 @Headers 어노테이션을 사용
    @POST("auth/login")
    suspend fun login(@Body request: Login): BaseResponse<LoginResponse>

    // 회원가입 API
    // 서버 오류 -parameter 발생
    //이메일 중복 검증 (GET 요청, URL 파라미터)
    @GET("users/email/is-unique")
    suspend fun checkEmailUnique(@Query("email") email: String): Response<BaseResponse<Boolean>>

    // 서버 사용자 인증 에러 발생
    //닉네임 중복 검증 (GET 요청, URL 파라미터)
    @GET("users/nickname/is-unique")
    suspend fun checkNicknameUnique(@Query("nickname") nickname: String): Response<BaseResponse<Boolean>>

    //이메일 인증 코드 발급 요청 (POST 요청, 이메일을 Body로 전달)
    @POST("users/email-verification-code")
    suspend fun requestEmailVerification(@Body emailRequest: Map<String, String>): Response<BaseResponse<Boolean>>

    //이메일 인증 코드 확인 (POST 요청, 이메일 & 인증 코드 전달)
    @POST("users/email-verification-code/confirm")
    suspend fun confirmEmailVerification(@Body verificationRequest: Map<String, String>): Response<BaseResponse<Boolean>>

    //회원가입 (POST 요청, 전체 회원 정보 전달)
    @POST("users")
    suspend fun registerUser(@Body request: Join): Response<BaseResponse<Boolean>>
}