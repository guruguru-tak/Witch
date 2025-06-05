package com.ssafy.witch.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.witch.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {
    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //1초 뒤에 다음 메인화면이 나오도록 설정
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 1000)
    }
}
