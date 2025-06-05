package com.ssafy.witch.data.remote.network

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.data.local.SharedPreferencesUtil
import com.ssafy.witch.data.model.dto.RefreshToken
import com.ssafy.witch.data.model.response.ErrorResponse
import com.ssafy.witch.data.remote.AuthService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.tokenService
import com.ssafy.witch.ui.LoginActivity
import com.ssafy.witch.ui.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

private const val TAG = "TokenAuthenticator"
class TokenAuthenticator(
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : Authenticator {

    private var isForceLoggingOut = false

    override fun authenticate(route: Route?, response: Response): Request? {
        if (getRetryCount(response) >= MAX_RETRY_COUNT) {
            forceLogout()
            return null
        }

        Log.d(TAG, "🚨 401 발생 → 요청 URL: ${response.request.url}")

        // 이미 갱신 중이면 대기
        synchronized(this) {
            if (TokenManager.isRefreshing) {
                Log.d(TAG, "⏳ 이미 토큰 갱신 중이므로 대기")
                return null
            }
        }

        // 토큰 갱신 시도
        val isOk = TokenManager.ensureValidToken()
        return if (isOk) {
            val newAccess = sharedPreferencesUtil.getAccessToken() ?: ""
            response.request.newBuilder()
                .header("Authorization", "Bearer $newAccess")
                .build()
        } else {
            forceLogout()
            null
        }
    }

    /**
     * 이전에 재시도한 횟수를 반환합니다.
     */
    private fun getRetryCount(response: Response): Int {
        var count = 0
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }
        Log.d(TAG, "현재 재시도 횟수: $count")
        return count
    }

    /**
     * Refresh Token이 없거나 재발급 실패 시 로그아웃 처리합니다.
     */
    private fun forceLogout() {
        synchronized(this) {
            if (isForceLoggingOut) return // 이미 로그아웃 처리 중이면 중단
            isForceLoggingOut = true
        }

        Log.e(TAG, "🚨 강제 로그아웃 처리: 저장된 토큰 모두 삭제")
        sharedPreferencesUtil.clearToken()

        val context = ApplicationClass.instance.applicationContext
        Handler(Looper.getMainLooper()).post {
            // 이미 로그아웃 상태라면 UI 처리를 중단
            if (!isForceLoggingOut) {
                Log.d(TAG, "🚫 forceLogout: 이미 로그아웃 처리 중이므로 토스트 표시 중단")
                return@post
            }

            // 토스트 메시지를 한 번만 표시
            if (!ApplicationClass.isLogoutToastShown) {
                Log.d(TAG, "🍞 Toast 표시: '로그인 정보가 만료되었습니다.'")
                Toast.makeText(
                    context,
                    "로그인 정보가 만료되었습니다.\n다시 로그인 해주세요.",
                    Toast.LENGTH_LONG
                ).show()
                ApplicationClass.isLogoutToastShown = true
            }

            val intent = Intent(context, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)

            synchronized(this) {
                isForceLoggingOut = false
            }
        }
    }

    companion object {
        private const val MAX_RETRY_COUNT = 3
    }
}
