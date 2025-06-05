package com.ssafy.witch.ui.mypage

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.ssafy.witch.R
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.DialogGroupMembersBinding
import com.ssafy.witch.databinding.DialogLogoutBinding
import com.ssafy.witch.databinding.FragmentProfileEditBinding
import com.ssafy.witch.databinding.FragmentPwdEditBinding
import com.ssafy.witch.databinding.FragmentSettingBinding
import com.ssafy.witch.ui.LoginActivity
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.MainActivity


class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::bind, R.layout.fragment_setting) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingFgLlChangePwd.setOnClickListener {
            val contentActivity = Intent(requireContext(), ContentActivity::class.java)
            contentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            contentActivity.putExtra("openFragment", 10)
            startActivity(contentActivity)
        }

        binding.settingFgLlLogout.setOnClickListener {
            showLogoutDialog()
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun showLogoutDialog() {
        val dialogBinding= DialogLogoutBinding.inflate(layoutInflater)

        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        dialogBinding.dlBtnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.dlBtnYes.setOnClickListener {
            ApplicationClass.sharedPreferencesUtil.clearToken()
            // 회원탈퇴와 달리 로컬 저장 데이터만 삭제
            ApplicationClass.sharedPreferencesUtil.deleteUser()

            // 로그인 액티비티로 전환 (기존 스택 모두 클리어)
            val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.dismiss()
            startActivity(intent)
            // 현재 액티비티 종료 (무한 반복 방지)
            requireActivity().finish()
        }
        dialog.show()

    }
}