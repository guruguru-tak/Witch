package com.ssafy.witch.data.remote.network

import android.util.Log
import android.widget.Toast
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.data.local.SharedPreferencesUtil
import com.ssafy.witch.data.model.dto.RefreshToken
import com.ssafy.witch.data.remote.RetrofitUtil
import kotlinx.coroutines.runBlocking
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.contracts.contract

object TokenManager {
    private const val TAG = "TokenManager"

    private val sharedPref: SharedPreferencesUtil by lazy {
        // 필요하다면 ApplicationClass.instance.applicationContext로 생성
        ApplicationClass.sharedPreferencesUtil
    }

    // 혹은 RetrofitUtil.tokenService / authService 등 필요한 것 직접 접근
    private val tokenService = RetrofitUtil.tokenService

    // 동시 접근 방지용 Lock
    private val lock = ReentrantLock()
    var isRefreshing = false  // 현재 갱신 중 여부

    /**
     * - 메인 액티비티 onResume 등에서 호출
     * - Authenticator(401 발생 시)에서도 호출
     * - “현재 토큰이 유효한지, 혹은 갱신이 필요한지” 체크 → 필요하면 갱신
     * - 갱신 결과 true면 “정상 토큰이 확보됨”, false면 “로그인 필요”
     */
    fun ensureValidToken(): Boolean {
        // 이미 다른 스레드에서 갱신 중이면, 잠깐 기다렸다가(또는 바로) 처리
        lock.withLock {
            lock.withLock {
                if (isRefreshing) {
                    Log.d(TAG, "⏳ 이미 토큰 갱신 중이므로 바로 반환")
                    return true
                }
                isRefreshing = true
            }

            return try {
                checkAndRefreshLogic()
            } finally {
                lock.withLock {
                    isRefreshing = false
                }
            }
        }

    }

    /**
     * 실제 토큰 만료 체크 & 재갱신 로직
     * - return true: 유효한 토큰 확보 (or 갱신 성공)
     * - return false: 토큰 만료 & 갱신 실패 → 로그인 필요
     */
    private fun checkAndRefreshLogic(): Boolean {
        val currentTime = System.currentTimeMillis() / 1000
        val accessTokenExpiresAt = sharedPref.getAccessTokenExpiresAt()
        val refreshTokenExpiresAt = sharedPref.getRefreshTokenExpiresAt()
        val refreshTokenIssuedAt = sharedPref.getRefreshTokenRenewAvailableSeconds()
        val storedRefreshToken = sharedPref.getRefreshToken()

        Log.d(TAG, "🔎 ensureValidToken() → currentTime=$currentTime")
        Log.d(TAG, "accessTokenExpiresAt=$accessTokenExpiresAt")
        Log.d(TAG, "refreshTokenExpiresAt=$refreshTokenExpiresAt")
        Log.d(TAG, "refreshTokenIssuedAt=$refreshTokenIssuedAt")
        Log.d(TAG, "refresh=$storedRefreshToken")

        // 1) Refresh Token 완전히 만료된 경우
        if (currentTime > refreshTokenExpiresAt) {
            Log.e(TAG, "❌ Refresh Token 만료 → 로그인 필요")
            return false
        }

        // 2) “Refresh Token 갱신 가능 시간(issuedAt) 지났다면” → renewRefreshToken
        if (currentTime > refreshTokenIssuedAt) {
            Log.d(TAG, "🔄 갱신 가능 기간 → renewRefreshToken 호출")
            val renewResult = renewRefreshTokenSync()
            return renewResult
        }

        // 3) 그 외: accessToken 만료 여부 체크
        if (currentTime > accessTokenExpiresAt) {
            Log.d(TAG, "🔄 Access Token 만료 → reissueAccessToken 호출")
            val reissueResult = reissueAccessTokenSync()
            return reissueResult
        }

        // 4) 모두 해당 없으면, 토큰 아직 유효
        Log.d(TAG, "✅ 현재 토큰 여전히 유효 → 별도 갱신 X")
        return true
    }

    /**
     * 리프레시 토큰 재갱신(동기) → 성공시 true, 실패시 false
     */
    fun renewRefreshTokenSync(): Boolean {
        val storedRefresh = sharedPref.getRefreshToken() ?: return false

        val response = runBlocking {
            runCatching {
                tokenService.renewRefreshToken(RefreshToken("Bearer $storedRefresh"))
            }.getOrNull()
        } ?: return false

        return if (response.isSuccessful) {
            val body = response.body()
            if (body?.success == true && body.data != null) {
                val data = body.data
                sharedPref.saveTokens(
                    data.accessToken,
                    data.accessTokenExpiresIn,
                    data.refreshToken,
                    data.refreshTokenExpiresIn,
                    data.refreshTokenRenewAvailableSeconds
                )
                Log.d(TAG, "✅ renewRefreshTokenSync 성공, 토큰 갱신 완료")
                true
            } else {
                Log.e(TAG, "❌ renewRefreshTokenSync 실패: ${body?.error?.errorMessage}")
                false
            }
        } else {
            Log.e(TAG, "❌ renewRefreshTokenSync 실패: HTTP ${response.code()}")
            false
        }
    }

    /**
     * 액세스 토큰 재갱신(동기) → 성공시 true, 실패시 false
     */
    fun reissueAccessTokenSync(): Boolean {
        val storedRefresh = sharedPref.getRefreshToken() ?: return false

        val response = runBlocking {
            runCatching {
                tokenService.reissueAccessToken(RefreshToken("Bearer $storedRefresh"))
            }.getOrNull()
        } ?: return false

        return if (response.isSuccessful) {
            val body = response.body()
            if (body?.success == true && body.data != null) {
                val data = body.data
                // 새 Access 토큰만 갱신하는 API일 수도 있으니
                sharedPref.saveAccessToken(data.accessToken, data.accessTokenExpiresIn)
                Log.d(TAG, "✅ reissueAccessTokenSync 성공, 토큰 갱신 완료")
                true
            } else {
                Log.e(TAG, "❌ reissueAccessTokenSync 실패: ${body?.error?.errorMessage}")
                false
            }
        } else {
            Log.e(TAG, "❌ reissueAccessTokenSync 실패: HTTP ${response.code()}")
            false
        }
    }
}