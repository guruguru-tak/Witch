package com.ssafy.witch.ui.appointment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.AppointmentDetailItem
import com.ssafy.witch.data.model.dto.AppointmentDetailItem.Participants
import com.ssafy.witch.data.model.dto.request.AppointmentRequest
import com.ssafy.witch.data.model.response.ErrorResponse
import com.ssafy.witch.data.model.response.LocationResponse
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.appointmentService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.userService
import kotlinx.coroutines.launch

private const val TAG = "AppointmentViewModel_Witch"
class AppointmentViewModel: ViewModel() {
    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String>
        get() = _toastMsg

    private val _fragmentIdx = MutableLiveData<Int>()
    val fragmentIdx: LiveData<Int>
        get() = _fragmentIdx

    private val _appointmentInfo = MutableLiveData<AppointmentDetailItem>()
    val appointmentInfo: LiveData<AppointmentDetailItem>
        get() = _appointmentInfo

    private val _locationList = MutableLiveData<List<LocationResponse.LocationInfo>>()
    val locationLists : LiveData<List<LocationResponse.LocationInfo>>
        get()= _locationList

    private val _groupId = MutableLiveData<String>()
    val groupId: LiveData<String>
        get() = _groupId

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId

    // bottomsheet 정보
    private val _name= MutableLiveData<String>()
    val name: LiveData<String>
        get()= _name

    private val _summary= MutableLiveData<String>()
    val summary: LiveData<String>
        get()= _summary

    private val _appointmentTime= MutableLiveData<String>()
    val appointmentTime: LiveData<String>
        get()= _appointmentTime

    private val _address= MutableLiveData<String>()
    val address: LiveData<String>
        get()= _address

    private val _latitude= MutableLiveData<Double>()
    val latitude: LiveData<Double>
        get()= _latitude

    private val _longitude= MutableLiveData<Double>()
    val longitude: LiveData<Double>
        get()= _longitude

    private val _appointmentStatus = MutableLiveData<String>()
    val appointmentStatus: LiveData<String>
        get() = _appointmentStatus

    private val _participants = MutableLiveData<MutableList<Participants>>()
    val participants : LiveData<MutableList<Participants>>
        get() = _participants

    private val _leader= MutableLiveData<MutableList<Participants>>()
    val leader : LiveData<MutableList<Participants>>
        get() = _leader

    fun appointmentClear() {
        _name.value = ""
        _summary.value = ""
        _appointmentTime.value = ""
        _latitude.value = Double.MIN_VALUE
        _longitude.value = Double.MIN_VALUE
        _address.value = ""
    }

    fun setGroupId(groupId: String) {
        _groupId.value = groupId
    }

    fun setName(name: String){
        _name.value = name
    }

    fun setSummary(summary: String){
        _summary.value = summary
    }

    fun setAppointmentTime(appointmentTime: String){
        _appointmentTime.value = appointmentTime
    }

    fun setLatitude(latitude: Double){
        _latitude.value = latitude
    }

    fun setLongitude(longitude: Double){
        _longitude.value = longitude
    }

    fun setAddress(address: String){
        _address.value = address
    }

    fun registerAppointment() {
        viewModelScope.launch {
            runCatching {
                appointmentService.registerAppointment(
                    groupId.value!!,
                    AppointmentRequest(
                        name.value!!,
                        summary.value!!,
                        appointmentTime.value!!,
                        latitude.value!!,
                        longitude.value!!,
                        address.value!!
                    )
                )
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    if (response.body()?.success == true) {
                        _toastMsg.value = "약속이 생성되었어요!"
                        appointmentClear()
                    }
                } else if(response.code() == 400) {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = errorBody?.let {
                        val type = object : TypeToken<BaseResponse<ErrorResponse>>() {}.type
                        Gson().fromJson<BaseResponse<ErrorResponse>>(it, type)
                    }
                    _toastMsg.value = errorResponse?.error?.errorMessage
                }
            }.onFailure { e ->
                Log.e(TAG, "registerAppointment() Exception: ${e.message}", e)
            }
        }
    }

    fun getAppointmentInfo(appointmentId: String) {
        viewModelScope.launch {
            runCatching {
                appointmentService.getAppointmentInfo(appointmentId)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    if (response.body()?.success == true) {
                        _appointmentInfo.value = response.body()?.data!!
                        _appointmentStatus.value = appointmentInfo.value?.appointmentStatus

                        val (leader, participants) = appointmentInfo.value?.participants
                            ?.partition { it.isLeader == true } ?: Pair(emptyList(), emptyList())
                        _leader.value = leader.toMutableList()
                        _participants.value = participants.toMutableList()
                    }
                } else if(response.code() == 400) {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = errorBody?.let {
                        val type = object : TypeToken<BaseResponse<ErrorResponse>>() {}.type
                        Gson().fromJson<BaseResponse<ErrorResponse>>(it, type)
                    }
                    _toastMsg.value = errorResponse?.error?.errorMessage
                }
            }.onFailure { e ->
                Log.e(TAG, "getAppointmentInfo() Exception: ${e.message}", e)
            }
        }
    }

    fun updateParticipantsInfo(appointmentId: String) {
        viewModelScope.launch {
            runCatching {
                appointmentService.getAppointmentInfo(appointmentId)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    if (response.body()?.success == true) {
                        val (leader, participants) = response.body()?.data?.participants
                            ?.partition { it.isLeader == true } ?: Pair(emptyList(), emptyList())
                        _leader.value = leader.toMutableList()
                        _participants.value = participants.toMutableList()
                        Log.d(TAG, "updateParticipantsInfo: ${_participants.value}")
                    }
                } else if(response.code() == 400) {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = errorBody?.let {
                        val type = object : TypeToken<BaseResponse<ErrorResponse>>() {}.type
                        Gson().fromJson<BaseResponse<ErrorResponse>>(it, type)
                    }
                    _toastMsg.value = errorResponse?.error?.errorMessage
                }
            }.onFailure { e ->
                Log.e(TAG, "getParticipantsInfo() Exception: ${e.message}", e)
            }
        }
    }

    fun getMyInfo() {
        viewModelScope.launch {
            runCatching {
                userService.getProfile()
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.data?.let { user ->
                        try {
                            _userId.value = user.userId
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    // 실패
                }
            }.onFailure {

            }
        }
    }

    fun deleteAppointment(appointmentId: String) {
        viewModelScope.launch {
            runCatching {
                appointmentService.deleteAppointment(appointmentId)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    if (response.body()?.success == true) {
                        _toastMsg.value = "약속을 삭제하였습니다!"
                        _fragmentIdx.value = 5
                    }
                } else if(response.code() == 400) {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = errorBody?.let {
                        val type = object : TypeToken<BaseResponse<ErrorResponse>>() {}.type
                        Gson().fromJson<BaseResponse<ErrorResponse>>(it, type)
                    }
                    _toastMsg.value = errorResponse?.error?.errorMessage
                }
            }.onFailure { e ->
                Log.e(TAG, "deleteAppointment() Exception: ${e.message}", e)
            }
        }
    }

    fun participateAppointment(appointmentId: String) {
        viewModelScope.launch {
            runCatching {
                appointmentService.participateAppointment(appointmentId)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    if (response.body()?.success == true) {
                        _toastMsg.value = "약속에 참여했습니다!"
                    }
                } else if(response.code() == 400) {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = errorBody?.let {
                        val type = object : TypeToken<BaseResponse<ErrorResponse>>() {}.type
                        Gson().fromJson<BaseResponse<ErrorResponse>>(it, type)
                    }
                    _toastMsg.value = errorResponse?.error?.errorMessage
                }
            }.onFailure { e ->
                Log.e(TAG, "participateAppointment() Exception: ${e.message}", e)
            }
        }
    }

    fun leaveAppointment(appointmentId: String) {
        viewModelScope.launch {
            runCatching {
                appointmentService.leaveAppointment(appointmentId)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    if (response.body()?.success == true) {
                        _toastMsg.value = "약속을 탈퇴했습니다!"
                    }
                } else if(response.code() == 400) {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = errorBody?.let {
                        val type = object : TypeToken<BaseResponse<ErrorResponse>>() {}.type
                        Gson().fromJson<BaseResponse<ErrorResponse>>(it, type)
                    }
                    _toastMsg.value = errorResponse?.error?.errorMessage
                }
            }.onFailure { e ->
                Log.e(TAG, "leaveAppointment() Exception: ${e.message}", e)
            }
        }
    }

    fun getLocationList(appointmentId: String) {
        viewModelScope.launch {
            runCatching {
                appointmentService.getAppointmentMembers(appointmentId)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                     _locationList.value = response.body()?.data?.positions!!
                } else if(response.code() == 400) {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = errorBody?.let {
                        val type = object : TypeToken<BaseResponse<ErrorResponse>>() {}.type
                        Gson().fromJson<BaseResponse<ErrorResponse>>(it, type)
                    }
                    Log.d(TAG, "getLocationList(): ${errorResponse?.data?.errorMessage}")
                }
            }.onFailure {  e ->
                Log.e(TAG, "getLocationList() Exception: ${e.message}", e)
            }
        }
    }
}