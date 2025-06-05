package com.ssafy.witch.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.response.GroupListResponse
import com.ssafy.witch.data.model.response.GroupResponse
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.groupService
import com.ssafy.witch.ui.GroupJoinActivity
import kotlinx.coroutines.launch

class GroupApprovalViewModel : ViewModel() {
    val _errorMessage = MutableLiveData<String>("")
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _group = MutableLiveData<GroupResponse>()
    val group: LiveData<GroupResponse>
        get() = _group


    fun getGroupPreview(groupId: String) {
        viewModelScope.launch {
            runCatching {
                groupService.getGroupPreview(groupId)
            }.onSuccess {
                if (it.isSuccessful) {
                    _group.value = it.body()?.data ?: GroupResponse("","","")
                } else if(it.code() == 400) {
                    it.errorBody()?.let { body ->
                        val data = Gson().fromJson(body.string(), BaseResponse::class.java)
                        _errorMessage.value = data.error.errorMessage
                    }
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun requestJoinGroup(groupId: String, context: GroupJoinActivity) {
        viewModelScope.launch {
            runCatching {
                groupService.requestJoinGroup(groupId)
            }.onSuccess {
                if (it.isSuccessful) {
                    _errorMessage.value = "그룹 가입 신청이 완료되었습니다."
                    context.finish()
                } else if(it.code() == 400) {
                    it.errorBody()?.let { body ->
                        val data = Gson().fromJson(body.string(), BaseResponse::class.java)
                        _errorMessage.value = data.error.errorMessage
                    }
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }



}