package com.ssafy.witch.data.model.response

data class GroupResponse(
    val cntLateArrival: Int,
    val groupId: String,
    val groupImageUrl: String,
    val isLeader: Boolean,
    val name: String
){
    constructor(groupId: String, groupImageUrl: String,name: String): this(0, groupId, groupImageUrl, false, name)
}