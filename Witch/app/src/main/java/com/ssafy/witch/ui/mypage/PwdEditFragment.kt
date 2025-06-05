package com.ssafy.witch.ui.mypage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentPwdEditBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.MainActivity

class PwdEditFragment : BaseFragment<FragmentPwdEditBinding>(FragmentPwdEditBinding::bind, R.layout.fragment_pwd_edit) {

    val viewModel: MyPageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 에러 메시지 관찰 → 에러 발생 시 토스트 띄우기
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        // editText 전부 비어있으면 처음부터 버튼 비활성화
        // 초기 버튼 상태 업데이트 (비밀번호 입력 전)
        updateJoinButtonState()

        binding.pwdEditFgBtnChangePwd.setOnClickListener {
            val password = binding.pwdEditFgEtOriginPwd.text.toString()
            val newPassword = binding.pwdEditFgEtNewPwd.text.toString()
            val passwordCheck = binding.pwdEditFgEtNewPwdTwice.text.toString()

            // 비밀번호 일치 여부 체크
            if (newPassword != passwordCheck) {
                Toast.makeText(requireContext(), "새 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 비밀번호 길이가 8~16자리인지 검사
            if (newPassword.length !in 8..16) {
                Toast.makeText(requireContext(), "비밀번호는 8~16자리여야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.editPassword(password, passwordCheck, requireActivity() as ContentActivity)
        }

        // 기존 비밀번호와 새로운 비밀번호 비교
        binding.pwdEditFgEtNewPwd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkOriginNewPasswordMatching()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 새로운 비밀번호와 새로운 비밀번호 같은지 비교
        binding.pwdEditFgEtNewPwdTwice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkNewPasswordMatching()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // 기존 비밀번호와 새로운 비밀번호 일치 여부 확인
    private fun checkOriginNewPasswordMatching() {
        val originPassword = binding.pwdEditFgEtOriginPwd.text.toString()
        val password = binding.pwdEditFgEtNewPwd.text.toString()

        binding.pwdEditFgTvSamePwd.isVisible =
            password.isNotBlank() && originPassword.isNotBlank() && password == originPassword

        updateJoinButtonState()
    }

    // 새로운 비밀번호와 새로운 비밀번호 중복 일치 여부 확인
    private fun checkNewPasswordMatching() {
        val password = binding.pwdEditFgEtNewPwd.text.toString()
        val passwordCheck = binding.pwdEditFgEtNewPwdTwice.text.toString()

        binding.pwdEditFgTvPasswordDuplFail.isVisible =
            password.isNotBlank() && passwordCheck.isNotBlank() && password != passwordCheck

        updateJoinButtonState()
    }

    /**
     * 회원가입 버튼 활성화 상태 업데이트
     */
    private fun updateJoinButtonState() {
        val originPassword = binding.pwdEditFgEtOriginPwd.text.toString()
        val password = binding.pwdEditFgEtNewPwd.text.toString()
        val passwordCheck = binding.pwdEditFgEtNewPwdTwice.text.toString()

        // 1. 기본 입력값이 모두 존재해야 함
        val isInputValid = originPassword.isNotBlank() && password.isNotBlank() && passwordCheck.isNotBlank()

        // 2. 비밀번호가 일치해야 함
        val isPasswordMatching = password == passwordCheck

        // 3. 기존 비밀번호가 신규 비밀번호와 달라야 함
        val isNotSameOriginNewPassword = originPassword != password

        // 4. 비밀번호가 8자 이상이어야 함
        val isLengthValid = password.length >= 8

        // 5. 모든 조건이 만족할 때만 회원가입 버튼 활성화
        val isJoinEnabled = isInputValid && isPasswordMatching && isNotSameOriginNewPassword && isLengthValid

        binding.pwdEditFgBtnChangePwd.isEnabled = isJoinEnabled
        if (isJoinEnabled) {
            binding.pwdEditFgBtnChangePwd.setBackgroundColor(resources.getColor(R.color.witch_green, null))
        } else {
            binding.pwdEditFgBtnChangePwd.setBackgroundColor(resources.getColor(R.color.witch_dark_gray, null))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}