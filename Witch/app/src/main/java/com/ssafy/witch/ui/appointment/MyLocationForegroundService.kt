package com.ssafy.witch.ui.appointment

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.response.ErrorResponse
import com.ssafy.witch.data.model.response.LocationResponse
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.appointmentService
import kotlinx.coroutines.launch

private const val TAG = "MyLocationForegroundService_Witch"
class MyLocationForegroundService : LifecycleService() {

    companion object {
        private val _locationData = MutableLiveData<LatLng>()
        val locationData: LiveData<LatLng> get() = _locationData
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var appointmentId: String

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: 위치 서비스 시작됨")

        startForeground(101, createNotification())

        // 위치 클라이언트 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 위치 요청 설정 (3초마다 업데이트)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setMinUpdateIntervalMillis(1000)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation ?: return
                _locationData.value = LatLng(location.latitude, location.longitude)
                sendLocationToServer(location)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        appointmentId = intent?.getStringExtra("appointmentId") ?: ""

        // 위치 권한 확인 후 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "위치 권한 없음 - 서비스 종료")
            stopSelf()
            return START_STICKY
        }

        startForeground(100, createNotification())

        // 위치 업데이트 시작
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        return START_REDELIVER_INTENT

    }

    private fun sendLocationToServer(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        Log.d(TAG, "sendLocationToServer: 나의 위치 $latitude, $longitude")
        lifecycleScope.launch {
            runCatching {
                appointmentService.updateLocation(appointmentId, LocationResponse.LocationInfo(latitude, longitude))
            }.onSuccess { response ->
                if(response.isSuccessful) {
                    Log.d(TAG, "sendLocationToServer: 위치 전송 성공~")
                } else{
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = errorBody?.let {
                        val type = object : TypeToken<BaseResponse<ErrorResponse>>() {}.type
                        Gson().fromJson<BaseResponse<ErrorResponse>>(it, type)
                    }
                    Log.d(TAG, "sendLocationToServer: error: ${errorResponse?.data?.errorMessage}")
                }
            }.onFailure { e ->
                Log.e(TAG, "sendLocationToServer() Exception: ${e.message}", e)
            }
        }
        
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: 위치 업데이트 중지")
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun createNotification(): Notification {
        val channel = NotificationChannel(
            "location_channel",
            "위치 서비스",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        return NotificationCompat.Builder(this, "location_channel")
            .setContentTitle("위치 서비스 실행 중")
            .setContentText("위치 정보를 업데이트하고 있습니다.")
            .setSmallIcon(R.drawable.location)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .setOngoing(true)
            .build()
    }

}