package com.ssafy.witch.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ssafy.witch.R
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentProfileEditBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.group.EditViewModel
import com.ssafy.witch.util.ImagePicker
import kotlinx.coroutines.launch


private const val TAG = "ProfileEditFragment"
class ProfileEditFragment : BaseFragment<FragmentProfileEditBinding>(FragmentProfileEditBinding::bind, R.layout.fragment_profile_edit) {
    private val viewModel: EditViewModel by viewModels()
    private lateinit var imagePickerUtil: ImagePicker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserver()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun initView(){
        binding.profileEditFgEtNickname.setText(ApplicationClass.sharedPreferencesUtil.getUser().nickname)
        Glide.with(binding.root)
            .load(ApplicationClass.sharedPreferencesUtil.getUser().profileImageUrl)
            .into(binding.profileEditFgIvProfileImage)

        initImage()

        binding.profileEditFgBtnNameChange.setOnClickListener {
            viewModel.editProfileName(binding.profileEditFgEtNickname.text.toString(), requireContext() as ContentActivity)
        }

        binding.profileEditFgBtnDuplCheck.setOnClickListener {
            viewModel.checkDupl(binding.profileEditFgEtNickname.text.toString(), "profile", requireContext() as ContentActivity).apply {
                binding.profileEditFgBtnNameChange.isEnabled = true
            }
        }

        binding.profileEditFgEtNickname.doOnTextChanged { text, start, before, count ->
            binding.profileEditFgBtnNameChange.isEnabled = false
        }
    }

    fun initImage(){

        binding.profileEditFgBtnImageDelete.setOnClickListener {
            viewModel.deleteFile()
            binding.profileEditFgIvProfileImage.setImageResource(R.drawable.box_style)
        }

        binding.profileEditFgBtnPhotoChange.setOnClickListener {
            lifecycleScope.launch {
                viewModel.uploadImage("profile", requireContext() as ContentActivity)
            }
        }

        imagePickerUtil = ImagePicker(this) { uri ->
            viewModel.setFile(uri)
            Glide.with(requireContext())
                .load(uri)
                .into(binding.profileEditFgIvProfileImage)
        }

        binding.profileEditFgBtnImageUpload.setOnClickListener {
            imagePickerUtil.checkPermissionAndOpenGallery()
        }

    }

    fun initObserver(){
        viewModel.errorMessage.observe(viewLifecycleOwner, {
            if(!it.isNullOrBlank()){
                showCustomToast(viewModel.errorMessage.value.toString())
            }
        })
    }
}
