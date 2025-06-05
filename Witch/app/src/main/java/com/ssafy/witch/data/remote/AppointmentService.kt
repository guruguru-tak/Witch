package com.ssafy.witch.data.remote

import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.AppointmentDetailItem
import com.ssafy.witch.data.model.dto.request.AppointmentRequest
import com.ssafy.witch.data.model.response.AppointmentResponse
import com.ssafy.witch.data.model.response.LocationResponse
import com.ssafy.witch.data.model.response.MyAppointmentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AppointmentService {

    @GET("/appointments/me")
    suspend fun getMyAppointments(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<BaseResponse<MyAppointmentResponse>>

    @GET("/appointments/me")
    suspend fun getNextAppointments(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<BaseResponse<MyAppointmentResponse>>

    @GET("/groups/{groupId}/appointments")
    suspend fun getGroupAppointments(
        @Path("groupId") groupId: String,
    ): Response<BaseResponse<MyAppointmentResponse>>

    @GET("/appointments/{appointmentId}")
    suspend fun getAppointmentInfo(
        @Path("appointmentId") appointmentId: String
    ): Response<BaseResponse<AppointmentDetailItem>>

    @GET("/appointments/{appointmentId}/members/position")
    suspend fun getAppointmentMembers(
        @Path("appointmentId") appointmentId: String
    ): Response<BaseResponse<LocationResponse>>

    @POST("/groups/{groupId}/appointments")
    suspend fun registerAppointment(
        @Path("groupId") groupId: String,
        @Body appointmentRequest: AppointmentRequest
    ): Response<BaseResponse<Boolean>>

    @POST("/appointments/{appointmentId}/members")
    suspend fun participateAppointment(
        @Path("appointmentId") appointmentId: String
    ): Response<BaseResponse<Boolean>>

    @DELETE("/appointments/{appointmentId}/members/me")
    suspend fun leaveAppointment(
        @Path("appointmentId") appointmentId: String
    ): Response<BaseResponse<Boolean>>

    @DELETE("/appointments/{appointmentId}")
    suspend fun deleteAppointment(
        @Path("appointmentId") appointmentId: String
    ): Response<BaseResponse<Boolean>>

    @PUT("/appointments/{appointmentId}/members/me/position")
    suspend fun updateLocation(
        @Path("appointmentId") appointmentId: String,
        @Body location: LocationResponse.LocationInfo
    ): Response<BaseResponse<Boolean>>
}
