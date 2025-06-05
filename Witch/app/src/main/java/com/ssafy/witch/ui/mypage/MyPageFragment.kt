package com.ssafy.witch.ui.mypage

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.ssafy.witch.R
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentGroupEditBinding
import com.ssafy.witch.databinding.FragmentMyPageBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.LoginActivity
import com.ssafy.witch.ui.MainActivity


private const val TAG = "MyPageFragment"
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::bind, R.layout.fragment_my_page) {

    val viewModel: MyPageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserver()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun initObserver(){
        viewModel.errorMessage.observe(viewLifecycleOwner, {
            if(!it.isNullOrBlank()){
                showCustomToast(viewModel.errorMessage.value.toString())
            }
        })
    }

    fun initView(){
        binding.mypageFgBtnProfileEdit.setOnClickListener {
            val contentActivity = Intent(requireContext(), ContentActivity::class.java)
            contentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            contentActivity.putExtra("openFragment", 3)
            startActivity(contentActivity)
        }


        binding.mypageFgLlPrivateSetting.setOnClickListener {
            (requireActivity() as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_flayout, SettingFragment()).addToBackStack("").commit()
        }

        viewModel.user.observe(viewLifecycleOwner, {
            binding.mypageFgTvNickname.text = it.nickname
            Glide.with(binding.root)
                .load(it.profileImageUrl)
                .into(binding.mypageFgIvProfileImage)
        })

    }

    override fun onResume() {
        super.onResume()

        viewModel.getProfile()
    }





}