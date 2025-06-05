package com.ssafy.witch.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.data.local.SharedPreferencesUtil
import com.ssafy.witch.data.model.dto.Login
import com.ssafy.witch.data.model.dto.RefreshToken
import com.ssafy.witch.data.model.response.ErrorResponse
import com.ssafy.witch.data.remote.AuthService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.authService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.tokenService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "LoginFragmentViewModel"
class LoginFragmentViewModel(application: Application): AndroidViewModel(application) {

    val sharedPreferencesUtil = SharedPreferencesUtil(application.applicationContext)

    // 이메일, 닉네임, JWT 토큰 로그인 처리
    // firebase 토큰 로직 최적화 필요
    fun login(email:String, password: String, onResult: (Boolean, String?) -> Unit) {
        //API 호출은 IO 스레드에서 실행
        viewModelScope.launch(Dispatchers.IO) {

            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (it.isSuccessful) {
                    val fcmToken = it.result ?: ""
                    Log.d(TAG, "login fcmtoken: $fcmToken")

                    // FCM 토큰 발급 완료 후 API 호출
                    viewModelScope.launch {
                        runCatching {
                            authService.login(Login(email, fcmToken, password))
                        }.onSuccess { response ->
                            if (response.success) {
                                response.data?.let { data ->
                                    Log.d(TAG, "서버에서 받은 accessTokenExpiresIn: ${data.accessTokenExpiresIn}")
                                    Log.d(TAG, "서버에서 받은 refreshTokenExpiresIn: ${data.refreshTokenExpiresIn}")

                                    // 저장 전에 로깅
                                    val currentTime = System.currentTimeMillis() / 1000
                                    Log.d(TAG, "현재 시간: $currentTime")
                                    Log.d(TAG, "계산된 AccessToken 만료 시간: ${currentTime + data.accessTokenExpiresIn}")
                                    Log.d(TAG, "계산된 RefreshToken 만료 시간: ${currentTime + data.refreshTokenExpiresIn}")

                                    sharedPreferencesUtil.saveTokens(
                                        data.accessToken,
                                        data.accessTokenExpiresIn,
                                        data.refreshToken,
                                        data.refreshTokenExpiresIn,
                                        data.refreshTokenRenewAvailableSeconds
                                    )
                                    Log.d(TAG, "access: ${data.accessToken}")
                                    Log.d(TAG, "a-expire: ${data.accessTokenExpiresIn}")
                                    Log.d(TAG, "refresh: ${data.refreshToken}")
                                    Log.d(TAG, "r-expire: ${data.refreshTokenExpiresIn}")
                                    Log.d(TAG, "login_avail: ${data.refreshTokenRenewAvailableSeconds}")
                                    onResult(true, data.accessToken)

                                } ?: run {
                                    onResult(false, "알 수 없는 오류 발생")
                                }
                            } else {
                                onResult(false, response.error.errorMessage)
                            }
                        }.onFailure { exception ->
                            onResult(false, exception.message ?: "로그인 중 오류 발생")
                        }
                    }
                } else {
                    Log.e(TAG, "Failed to get FCM token", it.exception)
                    onResult(false, "FCM 토큰 발급 실패")
                }
            }
        }
    }
}