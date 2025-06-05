package com.ssafy.witch.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ssafy.witch.R
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.base.ApplicationClass.Companion.sharedPreferencesUtil
import com.ssafy.witch.data.local.SharedPreferencesUtil
import com.ssafy.witch.data.remote.LocationWorker
import com.ssafy.witch.data.remote.network.TokenManager
import com.ssafy.witch.databinding.ActivityMainBinding
import com.ssafy.witch.ui.group.GroupCreateFragment
import com.ssafy.witch.ui.group.GroupEditFragment
import com.ssafy.witch.ui.auth.LoginFragmentViewModel
import com.ssafy.witch.ui.group.GroupFragment
import com.ssafy.witch.ui.group.GroupListFragment
import com.ssafy.witch.ui.home.HomeFragment
import com.ssafy.witch.ui.mypage.MyPageFragment
import com.ssafy.witch.ui.snack.SnackCreateFragment
import java.util.concurrent.TimeUnit


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    // 메인액티비티 자동로그인 후 토큰만료시간에 따른 토큰 재발급 및 자동 로그아웃 로직 위한 import
    private lateinit var loginViewModel: LoginFragmentViewModel

    private val mainBinding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        startWorkManager()

//        ApplicationClass.sharedPreferencesUtil.clearToken()
        // ViewModel 초기화 (토큰 재발급 함수 사용)
        loginViewModel = ViewModelProvider(this).get(LoginFragmentViewModel::class.java)

        val fragmentIdx = intent.getIntExtra("moveFragment", -1)
        if (fragmentIdx != -1) {
            moveFragment(fragmentIdx)
            intent.removeExtra("moveFragment")
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_flayout, HomeFragment())
                .commit()
        }

        mainBinding.mainABn.selectedItemId = R.id.bn_home

        mainBinding.mainABn.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.bn_home -> {
                    openFragment(1)
                    true
                }
                R.id.bn_group -> {
                    openFragment(2)
                    true
                }
                R.id.bn_mypage -> {
                    openFragment(3)
                    true
                }
                else -> false
            }

        }
        mainBinding.mainABn.setOnItemReselectedListener { item ->
            if(mainBinding.mainABn.selectedItemId != item.itemId){
                mainBinding.mainABn.selectedItemId = item.itemId
            }
        }

    }

    override fun onResume() {
        super.onResume()
        // 프래그먼트 전환 시 토큰 유효성 체크
        // 리프레쉬 토큰 갱신은 onResume에서 프로액티브하게 처리,  포그라운드 진입 시점에서, refresh token의 만료 또는 갱신 가능 조건을 미리 체크
        Log.d(TAG, "onResume: 리프레쉬 토큰 체크")
        val valid = TokenManager.ensureValidToken()
        if (!valid) {
            // → Refresh Token 만료 or 갱신 실패
            // 로그인 화면 이동 + finish() 등
            Log.d(TAG, "❌ 토큰 갱신 실패 or 만료 → 로그인 필요")
            navigateToLogin()
        } else {
            // 토큰이 유효 or 갱신 성공
            Log.d(TAG, "✅ 토큰 유효 or 갱신 성공")
        }
    }

    fun openFragment(index: Int, id: String = "") {
        moveFragment(index, id)
    }

    private fun moveFragment(index:Int, id: String = "") {
        val transaction = supportFragmentManager.beginTransaction()
        when(index){
            1 -> transaction.replace(R.id.main_flayout, HomeFragment())
            2 -> transaction.replace(R.id.main_flayout, GroupListFragment())
            3 -> transaction.replace(R.id.main_flayout, MyPageFragment())
            5 -> transaction.replace(R.id.main_flayout, GroupFragment.newInstance("groupId", id))
                .addToBackStack(null)
            6 -> transaction.replace(R.id.main_flayout, GroupCreateFragment())
                .addToBackStack(null)

            7 -> transaction.replace(R.id.main_flayout, GroupEditFragment())
                .addToBackStack(null)

            8 -> transaction.replace(R.id.main_flayout, SnackCreateFragment())
        }
        transaction.commit()
    }

    //로그인 액티비티 이동 함수
    private fun navigateToLogin() {
        //자동 로그아웃 시 토큰 다 날리기
        val sharedPref = SharedPreferencesUtil(application.applicationContext)
        sharedPref.clearToken()
        showToast("로그인 정보가 만료되었습니다.\n" +
                " 다시 로그인 해주세요.")
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    //토스트 메시지 출력 함수
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun startWorkManager() {
        val workManager = WorkManager.getInstance(this)

        workManager.cancelUniqueWork("LocationWorker")

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<LocationWorker>(30, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "LocationWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

}