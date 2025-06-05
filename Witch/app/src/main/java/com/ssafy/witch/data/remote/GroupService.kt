package com.ssafy.witch.data.remote

import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.GroupInfo
import com.ssafy.witch.data.model.dto.ObjectKey
import com.ssafy.witch.data.model.response.GroupJoinListResponse
import com.ssafy.witch.data.model.response.GroupListResponse
import com.ssafy.witch.data.model.response.GroupMemberResponse
import com.ssafy.witch.data.model.response.GroupResponse
import com.ssafy.witch.data.model.response.PresignedUrl
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupService {

    // 모임 사진 presigned url 요청
    @POST("/groups/group-image/presigned-url")
    suspend fun getPresignedUrl(
        @Body filename: String
    ) : Response<BaseResponse<PresignedUrl>>

    //모임 이름 중복검사
    @GET("/groups/name/is-unique")
    suspend fun checkGroupName(
        @Query("name") name: String
    ) : Response<Response<BaseResponse<Boolean>>>

    // 모임 생성
    @POST("groups")
    suspend fun createGroup(
        @Body groupInfo: GroupInfo,
    ) : Response<BaseResponse<String>>


    // 모임 가입 요청 수락
    @POST("groups/join-requests/{joinRequestId}/approve")
    suspend fun approveJoinRequest(
        @Path("joinRequestId") joinRequestId: String
    ) : Response<BaseResponse<String>>

    // 모임 가입 요청 거절
    @DELETE("groups/join-requests/{joinRequestId}")
    suspend fun rejectJoinRequest(
        @Path("joinRequestId") joinRequestId: String
    ) : Response<BaseResponse<String>>

    // 나의 모임 목록 조회
    @GET("groups/me")
    suspend fun getMyGroupList(

    ) : Response<BaseResponse<GroupListResponse>>

    //모임 가입 신청자 목록 조회
    @GET("groups/{groupId}/join-requests")
    suspend fun getJoinRequests(
        @Path("groupId") groupId: String,
    ) : Response<BaseResponse<GroupJoinListResponse>>

    // 모임 상세 조회
    @GET("groups/{groupId}")
    suspend fun getGroup(
        @Path("groupId") groupId: String,
    ) : Response<BaseResponse<GroupResponse>>

    // 모임 참여자 목록 조회
    @GET("groups/{groupId}/members")
    suspend fun getGroupMembers(
        @Path("groupId") groupId: String,
    ) : Response<BaseResponse<GroupMemberResponse>>

    // 모임 탈퇴
    @DELETE("/groups/{groupId}/members/me")
    suspend fun leaveGroup(
        @Path("groupId") groupId: String
    ) : Response<BaseResponse<String>>


    // 모임 이름 변경
    @PATCH("groups/{groupId}/name")
    suspend fun editGroupName(
        @Path("groupId") groupId: String,
        @Body name: String
    ) : Response<BaseResponse<String>>

    // 모임 사진 변경
    @PATCH("groups/{groupId}/image")
    suspend fun editGroupImage(
        @Path("groupId") groupId: String,
        @Body objectKey: ObjectKey?
    ) : Response<BaseResponse<String>>

    @GET("groups/{groupId}/preview")
    suspend fun getGroupPreview(
        @Path("groupId") groupId: String
    ) : Response<BaseResponse<GroupResponse>>

    @POST("groups/{groupId}/join-requests")
    suspend fun requestJoinGroup(
        @Path("groupId") groupId: String
    ) : Response<BaseResponse<String>>

}