package com.ssafy.witch.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ssafy.witch.R
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.base.BaseActivity
import com.ssafy.witch.databinding.ActivityContentBinding
import com.ssafy.witch.databinding.ActivityMainBinding
import com.ssafy.witch.databinding.FragmentGroupApprovalBinding
import com.ssafy.witch.ui.auth.LoginFragmentViewModel
import com.ssafy.witch.ui.group.GroupApprovalViewModel
import com.ssafy.witch.ui.group.GroupCreateFragment
import com.ssafy.witch.ui.group.GroupEditFragment
import com.ssafy.witch.ui.group.GroupFragment
import com.ssafy.witch.ui.group.GroupListFragment
import com.ssafy.witch.ui.home.HomeFragment
import com.ssafy.witch.ui.home.HomeViewModel
import com.ssafy.witch.ui.mypage.MyPageFragment
import com.ssafy.witch.ui.snack.SnackCreateFragment

private const val TAG = "GroupJoinActivity"
class GroupJoinActivity : BaseActivity<FragmentGroupApprovalBinding>(FragmentGroupApprovalBinding::inflate) {

    // 메인액티비티 자동로그인 후 토큰만료시간에 따른 토큰 재발급 및 자동 로그아웃 로직 위한 import
    private lateinit var loginViewModel: LoginFragmentViewModel

    private val viewModel: GroupApprovalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val action: String? = intent?.action
        val data: Uri? = intent?.data
        Log.d(TAG, "onCreate: $data")

        loginViewModel = ViewModelProvider(this).get(LoginFragmentViewModel::class.java)

        // 앱 시작 시 또는 액티비티 진입 시 토큰 유효성 체크

        viewModel.getGroupPreview(data?.getQueryParameter("groupId") ?: "")


        binding.groupApprovalFgBtnRequest.setOnClickListener {
            val groupId = data?.getQueryParameter("groupId")

            if(ApplicationClass.sharedPreferencesUtil.getAccessToken() == null){
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("state", 1)
                startActivity(intent)
            } else if (groupId != null) {
                viewModel.requestJoinGroup(groupId, this)
            }
        }

        initObserver()
    }

    fun initObserver() {
        viewModel.group.observe(this) {
            binding.groupApprovalFgTvGroupExName.text = it.name
            Glide.with(binding.root)
                .load(it.groupImageUrl)
                .into(binding.groupApprovalFgIvGroupImage)
                .onLoadFailed(resources.getDrawable(R.drawable.pot_icon))
        }

        viewModel.errorMessage.observe(this) {
            if (!it.isNullOrBlank()){
                showCustomToast(viewModel.errorMessage.value.toString())
            }
        }
    }

}