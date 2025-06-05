package com.ssafy.witch.base

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ssafy.witch.data.local.SharedPreferencesUtil
import com.ssafy.witch.data.remote.network.TokenAuthenticator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type
import com.google.gson.reflect.TypeToken
import com.ssafy.witch.data.model.response.ErrorResponse

// 앱이 실행될때 1번만 실행이 됩니다.
class ApplicationClass : Application() {

    // 코틀린의 전역변수 문법
    companion object {
        //ends with '/'
//        val API_URL = "http://i12d211.p.ssafy.io:30080/"
        val API_URL = "http://dukcode.iptime.org/"

        lateinit var sharedPreferencesUtil: SharedPreferencesUtil
        lateinit var retrofitLogin: Retrofit
        lateinit var retrofit: Retrofit

        var isLogoutToastShown = false
        // JWT Token Header 키 값
        const val ACCESS_TOKEN = "ACCESS-TOKEN"

        lateinit var instance: ApplicationClass
            private set
    }

    // 앱이 처음 생성되는 순간, SP를 새로 만들어주고, 레트로핏 인스턴스를 생성합니다.
    override fun onCreate() {
        super.onCreate()
        instance = this

        sharedPreferencesUtil = SharedPreferencesUtil(applicationContext)

        isLogoutToastShown = false

        //로그인 시만 작업하는 레트로핏
        // 1) 로그인 전용 (토큰 필요 없음)
        // 타임아웃 3초 로그인 전 사용 레트로핏
        val loginClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(3000, TimeUnit.MILLISECONDS)
            .connectTimeout(3000, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        retrofitLogin = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(loginClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        // 메인에서 작업하는 레트로핏
        // 레트로핏 인스턴스를 생성하고, 레트로핏에 각종 설정값들을 지정해줍니다.
        // 연결 타임아웃시간은 5초로 지정이 되어있고, HttpLoggingInterceptor를 붙여서 어떤 요청이 나가고 들어오는지를 보여줍니다.
        // 2) 로그인 후용 (TokenAuthenticator 적용)
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(AccessTokenInterceptor(sharedPreferencesUtil)) // JWT 자동 헤더 전송
            .authenticator(TokenAuthenticator(sharedPreferencesUtil))
            .build()

        // retrofit 이라는 전역변수에 API url, 인터셉터, Gson을 넣어주고 빌드해주는 코드
        // 이 전역변수로 http 요청을 서버로 보내면 됩니다.
        retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    }

    //GSon은 엄격한 json type을 요구하는데, 느슨하게 하기 위한 설정. success, fail이 json이 아니라 단순 문자열로 리턴될 경우 처리..
    val gson : Gson = GsonBuilder()
        .setLenient()
        .create()

    class AccessTokenInterceptor(private val sharedPreferencesUtil: SharedPreferencesUtil) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val builder = originalRequest.newBuilder()

            if (chain.request().url.toString()
                    .contains("witch-app.s3.ap-northeast-2.amazonaws.com")
            ) {
                return chain.proceed(builder.build())
            } else {

                val jwtToken = sharedPreferencesUtil.getAccessToken()

                if (!jwtToken.isNullOrEmpty()) {
                    Log.d("AccessTokenInterceptor", "✅ 인터셉터 실행! 저장된 Access Token: $jwtToken")
//                    return chain.proceed(
//                        chain.request().newBuilder()
//                            .header("Authorization", "Bearer $jwtToken")
//                            .build()
//                    )
                    builder.addHeader("Authorization", "Bearer $jwtToken")
                } else {
                    Log.d("AccessTokenInterceptor", "❌ Access Token 없음! Authorization 헤더 추가 안됨!")
                }

                val response = chain.proceed(builder.build())

                // 401 응답을 받으면 TokenAuthenticator 실행
                if (response.code == 401) {
                    Log.d("AccessTokenInterceptor", "🚨 401 응답 받음! TokenAuthenticator에서 처리 필요!")
                }

                return response
            }
        }
    }



}