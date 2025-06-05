package com.ssafy.witch.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.response.GroupListResponse
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.groupService
import kotlinx.coroutines.launch

class GroupListViewModel : ViewModel() {
    val _errorMessage = MutableLiveData<String>("")
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _groupList = MutableLiveData<List<GroupListResponse.GroupListItem>>()
    val groupList: LiveData<List<GroupListResponse.GroupListItem>>
        get() = _groupList


    fun getGroupList(){
        viewModelScope.launch {
            runCatching {
                groupService.getMyGroupList()
            }.onSuccess {
                if (it.isSuccessful) {
                    _groupList.value = it.body()?.data?.groups
                }else if(it.code() == 400){
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