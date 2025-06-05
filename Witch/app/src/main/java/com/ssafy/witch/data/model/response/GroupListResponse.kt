package com.ssafy.witch.data.model.response

import com.google.gson.annotations.SerializedName

data class GroupListResponse(
    @SerializedName("groups") val groups: List<GroupListItem>
){
    data class GroupListItem(
        val createdAt: String,
        val groupId: String,
        val groupImageUrl: String,
        val leader: Leader,
        val name: String
    ){
        data class Leader(
            val nickname: String,
            val profileImageUrl: String,
            val userId: String
        )
    }
}