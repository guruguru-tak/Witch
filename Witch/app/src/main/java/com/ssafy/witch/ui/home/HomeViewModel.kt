package com.ssafy.witch.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.MyAppointment
import com.ssafy.witch.data.model.response.MyAppointmentResponse
import com.ssafy.witch.data.remote.RetrofitUtil
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.appointmentService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.userService
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val _errorMessage = MutableLiveData<String>("")
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name


    private val _appointmentList = MutableLiveData<MyAppointmentResponse>()
    val appointmentList: LiveData<MyAppointmentResponse>
        get() = _appointmentList

    fun setName(name: String) {
        _name.value = name
    }


    fun getAllAppointments(year: Int, month: Int) {
        var appointmentLists: MutableList<MyAppointment> = mutableListOf()

        viewModelScope.launch {
            runCatching {
                appointmentService.getMyAppointments(year,month)
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.data?.let { appointments ->
                        try {
                            _appointmentList.value = appointments
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else if(it.code() == 400){
                    // 실패
                    it.errorBody()?.let { body ->
                        val data = Gson().fromJson(body.string(), BaseResponse::class.java)
                        _errorMessage.value = data.error.errorMessage
                    }
                }
            }.onFailure {

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
                            ApplicationClass.sharedPreferencesUtil.addUser(user)
                            _name.value = user.nickname
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else if(it.code() == 400) {
                    // 실패
                    it.errorBody()?.let { body ->
                        val data = Gson().fromJson(body.string(), BaseResponse::class.java)
                        _errorMessage.value = data.error.errorMessage
                    }
                }
            }.onFailure {
            }
        }
    }


}