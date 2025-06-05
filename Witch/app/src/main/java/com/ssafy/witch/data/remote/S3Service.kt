package com.ssafy.witch.data.remote

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Url

interface S3Service {
    @PUT
    suspend fun uploadFile(@Url url: String, @Body file: RequestBody?): Response<Unit>
}