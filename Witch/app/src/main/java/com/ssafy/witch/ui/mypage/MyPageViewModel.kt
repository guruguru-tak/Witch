package com.ssafy.witch.ui.mypage

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.EditPwd
import com.ssafy.witch.data.model.dto.MyAppointment
import com.ssafy.witch.data.model.dto.User
import com.ssafy.witch.data.model.response.MyAppointmentResponse
import com.ssafy.witch.data.remote.RetrofitUtil
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.appointmentService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.userService
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.MainActivity
import kotlinx.coroutines.launch

class MyPageViewModel : ViewModel() {
    val _errorMessage = MutableLiveData<String>("")
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user


    private val _appointmentList = MutableLiveData<MyAppointmentResponse>()
    val appointmentList: LiveData<MyAppointmentResponse>
        get() = _appointmentList

    fun setName(name: String) {
        _name.value = name
    }

    fun editPassword(password: String, newPassword: String, context:ContentActivity) {
        viewModelScope.launch {
            runCatching {
                userService.editPassword(EditPwd(password, newPassword))
            }.onSuccess {
                if (it.isSuccessful) {
                    Toast.makeText(context, "비밀번호를 변경했습니다.", Toast.LENGTH_SHORT).show()
                    context.finish()
                } else if(it.code() == 400) {
                    // 실패
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
    fun getProfile(){
        viewModelScope.launch {
            runCatching {
                userService.getProfile()
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.data?.let { user ->
                        try {
                            _user.value = user
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else if(it.code() == 400) {
                    it.errorBody()?.let { body ->
                        val data = Gson().fromJson(body.string(), BaseResponse::class.java)
                        _errorMessage.value = data.error.errorMessage
                    }
                    // 실패
                }
            }.onFailure {
            }
        }
    }





}