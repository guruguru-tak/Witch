package com.ssafy.witch.data.remote

import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.RefreshToken
import com.ssafy.witch.data.model.response.AccessTokenResponse
import com.ssafy.witch.data.model.response.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenService {
    //액세스 토큰 재발급 API
    @POST("auth/token/reissue")
    suspend fun reissueAccessToken(@Body request: RefreshToken): Response<BaseResponse<AccessTokenResponse>>

    //리프레시 토큰 재발급 API
    @POST("auth/token/renew")
    suspend fun renewRefreshToken(@Body request: RefreshToken): Response<BaseResponse<RefreshTokenResponse>>

}