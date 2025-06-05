package com.ssafy.witch.ui.snack

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.ssafy.witch.R
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentSnackBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.util.TimeConverter


class SnackFragment : BaseFragment<FragmentSnackBinding>(FragmentSnackBinding::bind, R.layout.fragment_snack) {

    val viewModel: SnackViewModel by viewModels()

    private var snackId = ""

    val mediaPlayer = MediaPlayer()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()

        initView()

    }

    fun initView(){

        binding.snackFgIvAudio.setOnClickListener {
            if(!viewModel.snack.value?.snackSoundUrl.isNullOrBlank() && !mediaPlayer.isPlaying){
                try {
                    mediaPlayer.setDataSource(viewModel.snack.value?.snackSoundUrl)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onDestroy() {
        super.onDestroy()
        if(mediaPlayer.isPlaying)
            mediaPlayer.stop()
            mediaPlayer.release()
    }


    fun initObserver(){
        viewModel.errorMessage.observe(viewLifecycleOwner, {
            if(!it.isNullOrBlank()){
                showCustomToast(viewModel.errorMessage.value.toString())
            }
        })

        viewModel.snack.observe(viewLifecycleOwner, {
            val time = TimeConverter().convertToLocalDateTime(it.createdAt)

            if( it.user.userId== ApplicationClass.sharedPreferencesUtil.getUser().userId) {
                binding.snackFgIbDelete.visibility = View.VISIBLE
                binding.snackFgIbDelete.setOnClickListener {
                    viewModel.deleteSnack(snackId, requireContext() as ContentActivity)
                }
            }else{
                binding.snackFgIbDelete.visibility = View.GONE
            }

            binding.snackFgTvUsername.text = it.user.nickname
            binding.snackFgTvCreatedAt.text = (time.monthValue-1).toString()+ "월 " + time.dayOfMonth.toString() +"일 "+ time.hour.toString() + "시 " + time.minute.toString() + "분"
            Glide.with(requireContext())
                .load(it.snackImageUrl)
                .into(binding.snackFgIvSnackImage)

            if (viewModel.snack.value?.snackSoundUrl.isNullOrBlank()) {
                binding.snackFgIvAudioText.text = "와작! 친구가 흘린 스낵을 주웠어요."
            }
        }
        )
    }

    override fun onResume() {
        super.onResume()

        viewModel.getSnack(snackId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            snackId = it.getString("snackId").toString()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(key:String, value:String) =
            SnackFragment().apply {
                arguments = Bundle().apply {
                    putString(key, value)
                }
            }
    }
}