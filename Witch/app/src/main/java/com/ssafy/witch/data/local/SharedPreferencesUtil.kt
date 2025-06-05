package com.ssafy.witch.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.ssafy.witch.data.model.dto.User
import java.time.LocalDateTime

private const val TAG = "SharedPreferencesUtil"
// 유저 정보 로컬에 저장하면 루팅시 평문상태로 개인정보 접근 가능하여 보안상 취약점 발생
// 로컬에선 비밀번호 저장 저장 금지, JWT 로 접근하기
// 단순 sharedPreferences는 평문 저장, EncryptedSharedPreferences 로 AES-256 암호화 보호
class SharedPreferencesUtil (context : Context) {
    private val SHARED_PREFERENCES_NAME = "witch_secure_preference"
//    val COOKIES_KEY_NAME = "cookies"

    // 서버에서 email과 password 로 로그인 성공시 보안을 위해 JWT로 토큰값 저장
    // JWT 토큰 키값
    val KEY_ACCESS_TOKEN = "access_token"
    val KEY_REFRESH_TOKEN = "refresh_token"
    val KEY_ACCESS_TOKEN_EXPIRES_AT = "accessTokenExpiresAt"
    val KEY_REFRESH_TOKEN_EXPIRES_AT = "refreshTokenExpiresAt"
    val KEY_REFRESH_TOKEN_RENEW_AVAILABLE_SECONDS = "refreshTokenRenewAvailableSeconds"

    val KEY_EMAIL = "email"
    val KEY_NICK = "nickname"
    val KEY_PROFILE_IMAGE = "profileImageUrl"
    val KET_USERID= "userId"

    //fcm-token 저장 필요시 구현

    // AES-256 암호화된 SharedPreferences 생성
    //// security-crypto 라이브러리 APP 단위 빌드그레이들 플러그인 추가
    private var preference: SharedPreferences

    init {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        preference = EncryptedSharedPreferences.create(
            context,
            SHARED_PREFERENCES_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    //JWT 토큰 저장 - JWT 방식이면 쿠키 필요 없음
    //Access Token이 만료되면 Refresh Token을 이용하여 재발급하면 자동 로그인 기능을 구현
    fun saveTokens(accessToken: String, accessTokenExpiresIn: Long, refreshToken: String, refreshTokenExpiresIn: Long, refreshTokenRenewAvailableSeconds: Long) {
        //현재시간 초단위로 기록
        val currentTime = System.currentTimeMillis() / 1000
        val editor = preference.edit()

        // 기존 Access Token 및 만료 시간 삭제
        editor.remove(KEY_ACCESS_TOKEN)
        editor.remove(KEY_ACCESS_TOKEN_EXPIRES_AT)
        editor.remove(KEY_REFRESH_TOKEN)
        editor.remove(KEY_REFRESH_TOKEN_EXPIRES_AT)
        editor.remove(KEY_REFRESH_TOKEN_RENEW_AVAILABLE_SECONDS)


        // 새로운 Access Token 및 만료 시간 저장
        editor.putString(KEY_ACCESS_TOKEN, accessToken)
        editor.putLong(KEY_ACCESS_TOKEN_EXPIRES_AT, currentTime + accessTokenExpiresIn)

        // Refresh Token 저장
        editor.putString(KEY_REFRESH_TOKEN, refreshToken)
        editor.putLong(KEY_REFRESH_TOKEN_EXPIRES_AT, currentTime + refreshTokenExpiresIn)
        editor.putLong(KEY_REFRESH_TOKEN_RENEW_AVAILABLE_SECONDS, currentTime + refreshTokenExpiresIn - refreshTokenRenewAvailableSeconds)

        // ✅ 동기적으로 즉시 반영
        val isSuccess = editor.commit()

        if (isSuccess) {
            Log.d(TAG, "✅ Tokens 저장 완료")
        } else {
            Log.e(TAG, "❌ Tokens 저장 실패")
        }
    }

    fun saveAccessToken(accessToken: String, accessTokenExpiresIn: Long) {
        Log.d(TAG, "🟢 새로운 Access Token 저장 시도: $accessToken")

        val currentTime = System.currentTimeMillis() / 1000

        val editor = preference.edit()

        // 기존 Access Token 삭제
        editor.remove(KEY_ACCESS_TOKEN)
        editor.remove(KEY_ACCESS_TOKEN_EXPIRES_AT)

        // 새로운 Access Token 및 만료 시간 저장
        editor.putString(KEY_ACCESS_TOKEN, accessToken)
        editor.putLong(KEY_ACCESS_TOKEN_EXPIRES_AT, currentTime + accessTokenExpiresIn)

        // ✅ 즉시 반영
        val isSuccess = editor.commit()

        if (isSuccess) {
            Log.d(TAG, "✅ Access Token 저장 완료: $accessToken")
        } else {
            Log.e(TAG, "❌ Access Token 저장 실패")
        }
    }

    //JWT Access Token 가져오기
    fun getAccessToken(): String? {
        return preference.getString(KEY_ACCESS_TOKEN, null)
    }

    //JWT Refresh Token 가져오기
    fun getRefreshToken(): String? {
        return preference.getString(KEY_REFRESH_TOKEN, null)
    }

    //Access Token 만료 시간 가져오기
    fun getAccessTokenExpiresAt(): Long {
        return preference.getLong(KEY_ACCESS_TOKEN_EXPIRES_AT, 0L)
    }

    //Refresh Token 만료 시간 가져오기
    fun getRefreshTokenExpiresAt(): Long {
        return preference.getLong(KEY_REFRESH_TOKEN_EXPIRES_AT, 0L)
    }

    fun getRefreshTokenRenewAvailableSeconds(): Long {
        return preference.getLong(KEY_REFRESH_TOKEN_RENEW_AVAILABLE_SECONDS, 0L)
    }

    //로그아웃 시 모든 JWT 토큰 삭제
    fun clearToken() {
        preference.edit().apply {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_ACCESS_TOKEN_EXPIRES_AT)
            remove(KEY_REFRESH_TOKEN_EXPIRES_AT)
            remove(KEY_REFRESH_TOKEN_RENEW_AVAILABLE_SECONDS)
            apply()
        }
        Log.d(TAG, "AccessToken: ${getAccessToken()}")
        Log.d(TAG, "RefreshToken: ${getRefreshToken()}")
        Log.d(TAG, "AccessTokenExpiresAt: ${getAccessTokenExpiresAt()}")
        Log.d(TAG, "RefreshTokenExpiresAt: ${getRefreshTokenExpiresAt()}")
        Log.d(TAG, "AvailableSeconds: ${getRefreshTokenRenewAvailableSeconds()}")
    }

    //사용자 정보 저장, user dto 생성하기
    fun addUser(user: User) {
        val editor =  preference.edit()
        editor.putString(KET_USERID, user.userId)
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_NICK, user.nickname)
        editor.putString(KEY_PROFILE_IMAGE, user.profileImageUrl)
        editor.apply()
    }

    fun getUser(): User {
        val email = preference.getString(KEY_EMAIL, "")
        if(email != "") {
            val nickname = preference.getString(KEY_NICK, "")
            val profileImage = preference.getString(KEY_PROFILE_IMAGE, "")
            val userId = preference.getString(KET_USERID, "")
            return User(userId!!, email!!, nickname!!, profileImage!!)
        } else{
          return User()
        }
    }

    fun deleteUser(){
        //preference 지우기
        val editor = preference.edit()
        editor.clear()
        editor.apply()
    }



}