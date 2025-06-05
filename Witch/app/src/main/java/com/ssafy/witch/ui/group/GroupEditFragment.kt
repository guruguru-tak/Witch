package com.ssafy.witch.ui.group

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentGroupEditBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.util.ImagePicker
import kotlinx.coroutines.launch


private const val TAG = "GroupEditFragment"
class GroupEditFragment : BaseFragment<FragmentGroupEditBinding>(FragmentGroupEditBinding::bind, R.layout.fragment_group_edit) {

    private val viewModel: EditViewModel by viewModels()
    private val groupViewModel: GroupViewModel by viewModels()

    private lateinit var imagePickerUtil: ImagePicker
    private var groupId = ""



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserver()

    }

    fun initView(){
        groupViewModel.getGroup(groupId)

        binding.groupEditFgBtnNameChange.setOnClickListener{
            viewModel.editGroupName(groupId, binding.groupEditFgEtNickname.text.toString() , requireContext() as ContentActivity)
        }


        binding.groupEditFgBtnPhotoChange.setOnClickListener {
            Log.d(TAG, "onViewCreated: ${viewModel.file.value}")
            lifecycleScope.launch {
                viewModel.uploadImage("edit", requireContext() as ContentActivity, groupId)
            }
        }

        imagePickerUtil = ImagePicker(this) { uri ->
            Log.d(TAG, "onViewCreated: $uri")
            viewModel.setFile(uri)
            Glide.with(requireContext())
                .load(uri)
                .into(binding.groupEditFgIvProfileImage)
        }

        binding.groupEditFgBtnImageUpload.setOnClickListener {
            imagePickerUtil.checkPermissionAndOpenGallery()
        }

        binding.groupEditFgEtNickname.doOnTextChanged { text, start, before, count ->
            binding.groupEditFgBtnNameChange.isEnabled = false
        }

        binding.groupEditFgBtnImageDelete.setOnClickListener {
            binding.groupEditFgIvProfileImage.setImageResource(R.drawable.circle_shape)
            viewModel.deleteFile()
        }

        binding.groupEditFgBtnDuplCheck.setOnClickListener {
            val newName = binding.groupEditFgEtNickname.text.toString()
            if (groupViewModel.group.value?.name == newName) {
                Toast.makeText(requireContext(), "현재 사용중인 이름입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if(newName.isBlank()){
                Toast.makeText(requireContext(), "그룹 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.checkDupl(newName, "group", requireContext() as ContentActivity).apply {
                    binding.groupEditFgBtnNameChange.isEnabled = true
                }
            }
        }
    }

    private fun initObserver(){
        viewModel.errorMessage.observe(viewLifecycleOwner, {
            if(!it.isNullOrBlank()){
                showCustomToast(viewModel.errorMessage.value.toString())
            }
        })

        groupViewModel.group.observe(viewLifecycleOwner){
            binding.groupEditFgEtNickname.setText(it.name)
            Glide.with(requireContext())
                .load(it.groupImageUrl)
                .into(binding.groupEditFgIvProfileImage)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            groupId = it.getString("groupId").toString()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(key:String, value:String) =
            GroupEditFragment().apply {
                arguments = Bundle().apply {
                    putString(key, value)
                }
            }
    }


}