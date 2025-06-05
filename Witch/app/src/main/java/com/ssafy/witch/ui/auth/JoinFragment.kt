package com.ssafy.witch.ui.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.data.model.dto.Join
import com.ssafy.witch.databinding.FragmentJoinBinding
import com.ssafy.witch.ui.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class JoinFragment : BaseFragment<FragmentJoinBinding>(
    FragmentJoinBinding::bind,
    R.layout.fragment_join
) {

    private lateinit var loginActivity: LoginActivity
    private val viewModel: JoinFragmentViewModel by viewModels()

    // 인증번호 타이머 관리용
    private var verificationTimerJob: Job? = null

    // 중복체크 여부에 따라 버튼 활성화, 비활성화 트리거
    private var isEmailChecked = false
    private var isNickChecked = false
    private var isEmailVerified = false // 이메일 인증 성공 여부

    // 이메일 인증 요청 횟수
    private var emailRequestCount = 0
    // 최대 요청 횟수
    private val maxEmailRequests = 3

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 입력값 변경 감지
        setupTextWatchers()

        // 이메일 중복 체크 버튼 (중복 체크 실패해도 인증번호 요청 버튼 활성화)
        binding.joinFgBtnDuplCheckEmail.setOnClickListener {
            val email = binding.joinFgEtEmail.text.toString()
            if (email.isBlank()) {
                showToast("이메일을 입력하세요.")
                return@setOnClickListener
            }

            if (emailRequestCount >= maxEmailRequests) {
                showToast("이메일 인증 요청 가능 횟수를 초과하였습니다.")
                binding.joinFgBtnEmailVerification.isGone = true // 버튼 숨기기
                binding.joinFgTvEmailVerificationCounter.isGone = true
                binding.joinFgTvEmailVerificationCountText.isGone = true
                return@setOnClickListener
            }

            viewModel.checkEmailUnique(email) { success, message ->
                if (success) {
                    showToast("사용 가능한 이메일입니다.")
                    binding.joinFgTvEmailDuplFail.isGone = true
                    binding.joinFgLlSendVeriNumGroup.isGone = false
                    isEmailChecked = true
                } else {
                    showToast("이메일 중복되었습니다.")
                    binding.joinFgTvEmailDuplFail.isGone = false
                    isEmailChecked = false
                    //API가 실패해도 인증번호 요청 버튼 활성화 - 테스트용
                    binding.joinFgLlSendVeriNumGroup.isGone = true
                    updateJoinButtonState()
                }

            }
        }

        // 인증번호 요청 버튼
        binding.joinFgBtnEmailVerification.setOnClickListener {
            val email = binding.joinFgEtEmail.text.toString()
            if (email.isBlank()) {
                showToast("이메일을 입력하세요.")
                return@setOnClickListener
            }
            viewModel.requestEmailVerification(email) { success, message ->
                if (success) {
                    showToast("인증번호가 전송되었습니다.")
                    emailRequestCount++

                    // 남은 요청 횟수 업데이트
                    binding.joinFgTvEmailVerificationCounter.text = (maxEmailRequests - emailRequestCount).toString()

                    if (emailRequestCount >= maxEmailRequests) {
                        binding.joinFgBtnEmailVerification.isGone = true // 버튼 숨기기
                        binding.joinFgTvEmailVerificationCounter.isGone = true
                        binding.joinFgTvEmailVerificationCountText.isGone = true
                    }
                } else {
                    showToast("인증번호 전송 실패")
                }
                binding.joinFgLlCheckVeriNumGroup.isVisible = true   // 인증번호 입력란 표시
                startVerificationTimer(599) // 10분 타이머 시작
            }
        }

        // 인증번호 확인 버튼
        binding.joinFgBtnDuplCheckVeriNum.setOnClickListener {
            val email = binding.joinFgEtEmail.text.toString()
            val verificationCode = binding.joinFgEtVeriNum.text.toString()
            if (verificationCode.isBlank()) {
                showToast("인증번호를 입력하세요.")
                return@setOnClickListener
            }
            viewModel.confirmEmailVerification(email, verificationCode) { success, message ->
                if (success) {
                    showToast("인증이 완료되었습니다.")
                    isEmailVerified = true // 인증 완료 플래그 설정
                    verificationTimerJob?.cancel() // 타이머 중지

                    binding.joinFgTvVeriNumTimer.isGone = true
                    binding.joinFgLlCheckVeriNumGroup.isGone = true
                    binding.joinFgLlSendVeriNumGroup.isGone = true

                    updateJoinButtonState() // 버튼 상태 업데이트
                } else {
                    showToast("인증 실패")
                }
            }
        }

        // 닉네임 중복 체크 버튼 (중복 체크 실패해도 테스트용으로 진행)
        binding.joinFgBtnDuplCheckNick.setOnClickListener {
            val nickname = binding.joinFgEtNick.text.toString()
            if (nickname.isBlank()) {
                showToast("닉네임을 입력하세요.")
                return@setOnClickListener
            }
            // 닉네임 길이가 2~8자리인지 검사
            if (nickname.length !in 2..8) {
                Toast.makeText(requireContext(), "닉네임은 2~8자리여야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.checkNicknameUnique(nickname) { success, message ->
                if (success) {
                    showToast("사용 가능한 닉네임입니다.")
                    //회원가입 버튼 활성화
                    isNickChecked = true
                    binding.joinFgTvNickDuplFail.isGone = true
                } else {
                    showToast("닉네임이 중복되었습니다.")
                    //회원가입 버튼 비활성화 색 변경
                    isNickChecked = false
                    binding.joinFgTvNickDuplFail.isGone = false
                }
                updateJoinButtonState()
            }
        }

        // 회원가입 버튼
        binding.joinFgBtnJoin.setOnClickListener {
            val email = binding.joinFgEtEmail.text.toString()
            val password = binding.joinFgEtPassword.text.toString()
            val passwordCheck = binding.joinFgEtPasswordCheck.text.toString()
            val nickname = binding.joinFgEtNick.text.toString()
            val emailVerificationCode = binding.joinFgEtVeriNum.text.toString()

            if (!isEmailChecked) {
                showToast("이메일 중복 체크를 해주세요.")
                return@setOnClickListener
            }
            if (!isNickChecked) {
                showToast("닉네임 중복 체크를 해주세요.")
                return@setOnClickListener
            }

            // 비밀번호 일치 여부 체크
            if (password != passwordCheck) {
                showToast("비밀번호가 일치하지 않습니다.")
                return@setOnClickListener
            }

            // 비밀번호 길이가 8~16자리인지 검사
            if (password.length !in 8..16) {
                Toast.makeText(requireContext(), "비밀번호는 8~16자리여야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val joinRequest = Join(email, password, nickname, emailVerificationCode)

            viewModel.registerUser(joinRequest) { success, message ->
                if (success) {
                    showToast("회원가입이 완료되었습니다.\n 로그인 후 이용해주세요.")
                    loginActivity.openFragment(3)
                } else {
                    showToast("회원가입에 실패하였습니다.")
                }
            }
        }

        binding.joinFgEtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkPasswordMatching()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.joinFgEtPasswordCheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkPasswordMatching()
            }

            override fun afterTextChanged(s: Editable?) {}
        })


    }

    // 인증번호 타이머 실행
    private fun startVerificationTimer(seconds: Int) {
        verificationTimerJob?.cancel()  // 기존 타이머 중지
        verificationTimerJob = CoroutineScope(Dispatchers.Main).launch {
            var remainingTime = seconds
            while (remainingTime >= 0) {
                val minutes = remainingTime / 60
                val seconds = remainingTime % 60
                binding.joinFgTvVeriNumTimer.text = String.format("%d:%02d", minutes, seconds)
                delay(1000L)
                remainingTime--
            }
            // 타이머 종료 시 UI 변경
            binding.joinFgTvVeriNumTimer.text = "시간 초과"
            binding.joinFgLlSendVeriNumGroup.isGone = false  // 인증번호 발송 버튼 활성화
        }
    }

    //토스트 메시지 출력 함수
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // 비밀번호 일치 여부 확인
    private fun checkPasswordMatching() {
        val password = binding.joinFgEtPassword.text.toString()
        val passwordCheck = binding.joinFgEtPasswordCheck.text.toString()

        binding.joinFgTvPasswordDuplFail.isVisible =
            password.isNotBlank() && passwordCheck.isNotBlank() && password != passwordCheck

        updateJoinButtonState()
    }

    /**
     * EditText 변경 감지 (이메일, 닉네임 수정 시 중복체크 다시 필요하도록 설정)
     */
    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateJoinButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.joinFgEtEmail.addTextChangedListener(textWatcher)
        binding.joinFgEtNick.addTextChangedListener(textWatcher)
        binding.joinFgEtPassword.addTextChangedListener(textWatcher)
        binding.joinFgEtVeriNum.addTextChangedListener(textWatcher)

        // 이메일, 닉네임이 변경되면 중복 체크 여부 리셋
        binding.joinFgEtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEmailChecked = false
                updateJoinButtonState()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.joinFgEtNick.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isNickChecked = false
                updateJoinButtonState()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * 회원가입 버튼 활성화 상태 업데이트
     */
    private fun updateJoinButtonState() {
        val email = binding.joinFgEtEmail.text.toString()
        val nickname = binding.joinFgEtNick.text.toString()
        val password = binding.joinFgEtPassword.text.toString()
        val passwordCheck = binding.joinFgEtPasswordCheck.text.toString()
        val verificationCode = binding.joinFgEtVeriNum.text.toString()

        // 1. 기본 입력값이 모두 존재해야 함
        val isInputValid = email.isNotBlank() && nickname.isNotBlank() &&
                password.isNotBlank() && passwordCheck.isNotBlank() && verificationCode.isNotBlank()

        // 2. 이메일과 닉네임 중복 체크가 완료되어야 함
        val isDuplicateChecked = isEmailChecked && isNickChecked

        // 3. 비밀번호가 일치해야 함
        val isPasswordMatching = password == passwordCheck

        // 4. 이메일 인증이 완료되어야 함
        val isVerificationCompleted = isEmailVerified

        // 5. 비밀번호가 8자 이상이어야 함
        val isLengthValid = password.length >= 8

        // 6. 모든 조건이 만족할 때만 회원가입 버튼 활성화
        val isJoinEnabled = isInputValid && isDuplicateChecked && isPasswordMatching && isVerificationCompleted && isLengthValid

        binding.joinFgBtnJoin.isEnabled = isJoinEnabled
        if (isJoinEnabled) {
            binding.joinFgBtnJoin.setBackgroundColor(resources.getColor(R.color.witch_green, null))
        } else {
            binding.joinFgBtnJoin.setBackgroundColor(resources.getColor(R.color.witch_dark_gray, null))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}