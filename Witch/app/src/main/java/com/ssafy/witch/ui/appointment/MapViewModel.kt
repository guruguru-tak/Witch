package com.ssafy.witch.ui.appointment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.AppointmentDetailItem
import com.ssafy.witch.data.model.response.ErrorResponse
import com.ssafy.witch.data.model.response.SnackResponse
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.appointmentService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.snackService
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

private const val TAG = "MapViewModel_Witch"
class MapViewModel : ViewModel(){
    private val _userStatus = MutableLiveData<Int>()
    val userStatus: LiveData<Int>
        get() = _userStatus

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String>
        get() = _toastMsg

    private val _remainderTime = MutableLiveData<Long?>()
    val remainderTime: LiveData<Long?>
        get() = _remainderTime

    private val _snackList = MutableLiveData<List<SnackResponse.SnackInfo>?>()
    val snackList: LiveData<List<SnackResponse.SnackInfo>?>
        get() = _snackList

    fun setRemainderTime(time: Long?) {
        _remainderTime.value = time
    }

    fun setUserStatus(userStatus: Int) {
        _userStatus.value = userStatus
    }

    fun getSnackList(appointmentId: String) {
        viewModelScope.launch {
            runCatching {
                snackService.getSnackList(appointmentId)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    Log.d(TAG, "getSnackList: ${response.body()?.data!!.snacks}")
                    _snackList.value = response.body()?.data!!.snacks
                } else if(response.code() == 400) {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = errorBody?.let {
                        val type = object : TypeToken<BaseResponse<ErrorResponse>>() {}.type
                        Gson().fromJson<BaseResponse<ErrorResponse>>(it, type)
                    }
                    Log.d(TAG, "getSnackList failed(): ${errorResponse?.data?.errorMessage}")
                }
            }.onFailure { e ->
                Log.e(TAG, "getSnackList() Exception: ${e.message}", e)
            }
        }
    }
}