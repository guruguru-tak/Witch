package com.ssafy.witch.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ssafy.witch.R
import com.ssafy.witch.base.ApplicationClass.Companion.sharedPreferencesUtil
import com.ssafy.witch.base.BaseActivity
import com.ssafy.witch.data.local.SharedPreferencesUtil
import com.ssafy.witch.data.remote.network.TokenManager
import com.ssafy.witch.databinding.ActivityLoginBinding
import com.ssafy.witch.ui.auth.JoinFragment
import com.ssafy.witch.ui.auth.LoginFragment
import com.ssafy.witch.ui.auth.LoginFragmentViewModel

private const val TAG = "LoginActivity"
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private lateinit var sharedPreferences: SharedPreferencesUtil
    private lateinit var loginViewModel: LoginFragmentViewModel

    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Witch)
        sharedPreferences = SharedPreferencesUtil(applicationContext)
        loginViewModel = ViewModelProvider(this).get(LoginFragmentViewModel::class.java)

        id = intent.getIntExtra("state", 0)

        // 최초 실행인 경우에만 토큰 검증 로직 실행
        if (savedInstanceState == null) {
            val accessToken = sharedPreferences.getAccessToken()
            val refreshToken = sharedPreferences.getRefreshToken()

            // AccessToken이 없는 경우 (최초 로그인 화면)
            if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
                openFragment(3) // LoginFragment 호출
            } else if (TokenManager.ensureValidToken()) {
                // 토큰이 유효하면 메인 화면으로 이동
                openFragment(1)
            } else {
                // 토큰이 유효하지 않으면 로그인 화면으로
                clearTokenLogin()
            }
        }

    }


    //로그인 액티비티 이동 함수
    private fun clearTokenLogin() {
        //자동 로그아웃 시 토큰 다 날리기
        val sharedPref = SharedPreferencesUtil(application.applicationContext)
        val refreshTokenExpiresAt = sharedPref.getRefreshTokenExpiresAt()

        if (refreshTokenExpiresAt != 0L) {
            showToast("로그인 정보가 만료되었습니다.\n" +
                    " 다시 로그인 해주세요.")
        }
        sharedPref.clearToken()
        openFragment(3,id)
    }

    //토스트 메시지 출력 함수
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // 로그인 시 해당하는 번호의 프래그먼트 지정
    fun openFragment(int: Int, id: Int = 0) {
        val transaction = supportFragmentManager.beginTransaction()
        when(int) {
            1 -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            2 -> transaction.replace(R.id.login_a_fl, JoinFragment())
                .addToBackStack(null)
            3 -> {
                // 회원가입한 뒤 돌아오면, 2번에서 addToBackStack해 놓은게 남아 있어서,
                // stack을 날려 줘야 한다. stack날리기.
                supportFragmentManager.popBackStack()
                transaction.replace(R.id.login_a_fl, LoginFragment.newInstance("state",id))
            }
        }
        transaction.commit()
    }

}