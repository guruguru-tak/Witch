package com.ssafy.witch.ui.snack

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ssafy.witch.R
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.MyAppointment
import com.ssafy.witch.data.model.dto.Snack
import com.ssafy.witch.data.model.dto.User
import com.ssafy.witch.data.model.response.ErrorResponse
import com.ssafy.witch.data.model.response.MyAppointmentResponse
import com.ssafy.witch.data.model.response.PresignedUrl
import com.ssafy.witch.data.remote.RetrofitUtil
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.appointmentService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.groupService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.s3Service
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.snackService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.userService
import com.ssafy.witch.data.remote.S3Service
import com.ssafy.witch.ui.ContentActivity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SnackViewModel : ViewModel() {
    val _errorMessage = MutableLiveData<String>("")
    val errorMessage: LiveData<String>
        get() = _errorMessage


    val _snack = MutableLiveData<Snack>(Snack("1", User("빵빵이"), "2025-02-11T20:52:15",  "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyNTAxMTBfOTIg%2FMDAxNzM2NDg3MDA1MTY4.yXmyvwy4HkXTitAJHSK-5LhqYgGUUo_vObedcwmikWMg.EOTbw8Qcd_5oW8TLy-xFUOsiXQY_ONAdRbxEilqjL24g.PNG%2F%25BD%25BA%25C5%25A9%25B8%25B0%25BC%25A6_2025-01-10_%25BF%25C0%25C8%25C4_2.30.00.png&type=sc960_832", "https://rr8---sn-3u-bh2zs.googlevideo.com/videoplayback?expire=1739545156&ei=5AWvZ9LWD-mRssUP6tbYkQo&ip=103.203.174.40&id=o-AOJulZY6-y-r6IZo6dliy1Jis7y-2etF0OA1Yovrhmbk&itag=18&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&bui=AUWDL3w_NZpu72CTHvOF0BVfG18CV9qVu-3u62XPopZRGHK5u-pgjadoxp2aKLKNsMzTLM466cyd6m2r&spc=RjZbSUP0uUaBVA57HMtWEyOQ0MxKbvpO04_e9kk5g2fkK3B6uyJZ8nM2VqikfDbZyg&vprv=1&svpuc=1&mime=video%2Fmp4&ns=sUBkYeCuyFXjy1jTSzZl-ngQ&rqh=1&gir=yes&clen=5539954&ratebypass=yes&dur=257.764&lmt=1724232979604123&fexp=24350590,24350737,24350825,24350827,24350961,24350977,24350978,24351064,24351082,24351132,24351184,24351201,51326932,51331020&c=MWEB&sefc=1&txp=5319224&n=QNG2DoHVrqCKMw&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cbui%2Cspc%2Cvprv%2Csvpuc%2Cmime%2Cns%2Crqh%2Cgir%2Cclen%2Cratebypass%2Cdur%2Clmt&sig=AJfQdSswRAIgKm0OK-WaBGpFBi6I6RYqtN3fZEPz7S8ZHnYcniqW3V8CIFxuWTMRUcOSLuZiEsyk1_OFY2H7d91qEdfffquBqtJ_&title=LUCY%20(%EB%A3%A8%EC%8B%9C)%20-%20%EC%95%84%EC%A7%80%EB%9E%91%EC%9D%B4%20%5B%EA%B0%80%EC%82%AC%2FLyrics%5D&rm=sn-h5moxufvg3n-itql7l,sn-h55es7l&rrc=79,104&req_id=fd5e568fbd02a3ee&cmsv=e&rms=rdu,au&redirect_counter=2&cms_redirect=yes&ipbypass=yes&met=1739523565,&mh=Yt&mip=14.46.141.161&mm=29&mn=sn-3u-bh2zs&ms=rdu&mt=1739523191&mv=m&mvi=8&pl=20&lsparams=ipbypass,met,mh,mip,mm,mn,ms,mv,mvi,pl,rms&lsig=AGluJ3MwRgIhAObxpiJVF0hTjDGsvmIzyalKJentOTbmE4awaZLI3pGcAiEAt-gyfGtsaev7v9qsCpHe44nsgd0QyYpdPxTr8OtSBfI%3D",0.0, 0.0,"","",""))
    val snack: LiveData<Snack>
        get() = _snack

    val _distance = MutableLiveData<Double>()
    val distance: LiveData<Double>
        get() = _distance

    fun setDistance(distance: Double) {
        _distance.value = distance
    }

    fun getSnack(appointmentId: String) {
        viewModelScope.launch {
            runCatching {
                snackService.getSnack(appointmentId)
            }.onSuccess {
                if (it.isSuccessful) {
                    _snack.value = it.body()?.data!!
                } else if(it.code() == 400) {
                    val data = Gson().fromJson(it.errorBody()?.string(), BaseResponse::class.java)
                    _errorMessage.value = data.error.errorMessage
                }
            }.onFailure {
                Log.d("SnackCreateViewModel", "getSnack: ${it.message}")
            }
        }
    }


    fun deleteSnack(snackId: String, context: ContentActivity) {
        viewModelScope.launch {
            runCatching {
                snackService.deleteSnack(snackId)
            }.onSuccess {
                if (it.isSuccessful) {
                    _errorMessage.value = "삭제되었습니다."
                    context.onBackPressed()
                } else if(it.code() == 400) {
                    val data = Gson().fromJson(it.errorBody()?.string(), BaseResponse::class.java)
                    _errorMessage.value = data.error.errorMessage

                }
            }.onFailure {
                Log.d("SnackCreateViewModel", "deleteSnack: ${it.message}")
            }
        }
    }


}