package com.ssafy.witch.ui.snack

import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.ssafy.witch.R
import com.ssafy.witch.databinding.DialogSnackRecordBinding
import java.io.File


private const val TAG = "SnackRecord"
class SnackRecord(private val fragment: Fragment,
                  private val viewModel: SnackCreateViewModel,
                  private val requestPermissionLauncher: ActivityResultLauncher<String>
) {
    private var audioName=""

    private  var mediaRecorder: MediaRecorder = MediaRecorder()

    private var mediaPlayer= MediaPlayer()

    private var path: String? = null

    private var file: File = File("")


    fun recordDialog() {
        val dialogBinding= DialogSnackRecordBinding.inflate(fragment.layoutInflater)
        dialogBinding.snackRecordDlTvFilePlay.isGone = true
        val dialog = Dialog(fragment.requireContext())
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        dialog.setOnDismissListener {
            stopRecord(true)
        }

        dialogBinding.snackRecordDlTvTitle.text = "스낵을 입력해주세요"

        dialogBinding.snackRecordDlBtnYes.setOnClickListener {
            viewModel.setAudio(File(audioName).toUri())
            dialog.dismiss()
        }

        dialogBinding.snackRecordDlBtnNo.setOnClickListener {
            viewModel.deleteAudio()
            dialog.dismiss()
        }


        dialogBinding.snackRecordDlIvRecord.setOnClickListener {
            initRecord(dialogBinding)

            if (audioName.isNotEmpty() && viewModel.recordState.value == false) {
                dialogBinding.snackRecordDlTvFilePlay.isGone = false
                dialogBinding.snackRecordDlTvFilePlay.setOnClickListener {
                    playRecord()
                }
            }else{
                dialogBinding.snackRecordDlTvFilePlay.isGone = true
            }

        }
        dialog.show()
    }

    fun initRecord(dialogBinding: DialogSnackRecordBinding) {
        if (viewModel.recordState.value == false) {


            if (ActivityCompat.checkSelfPermission(fragment.requireContext(), android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                startRecord(dialogBinding)
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO).apply {
                    if (ActivityCompat.checkSelfPermission(fragment.requireContext(), android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        startRecord(dialogBinding)
                    }
                }
            }


        } else {
            viewModel.setRecordState(false)
            dialogBinding.snackRecordDlTvFileText.text = "녹음 시작"
            dialogBinding.snackRecordDlIvRecord.setImageResource(R.drawable.record_start)
            stopRecord()
        }

    }

    fun startRecord(dialogBinding: DialogSnackRecordBinding) {
        mediaRecorder = MediaRecorder()
        viewModel.setRecordState(true)
        dialogBinding.snackRecordDlTvFileText.text = "녹음중"
        dialogBinding.snackRecordDlIvRecord.setImageResource(R.drawable.record_stop)
        path = fragment.requireContext().getExternalFilesDir("/")?.absolutePath // 🔥 path 설정

        audioName = "$path/${System.currentTimeMillis()}.mp3" // 🔥 path 활용

        file = File(audioName)
        file.parentFile?.mkdirs()
        file.createNewFile()


        try {
            mediaRecorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC) // 🔥 에러 발생 지점
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioName)
                prepare()
                start()
            }
        } catch (e: Exception) {
            Log.e(TAG, "startRecord: 녹음 시작 실패", e)
        }
    }



    fun stopRecord(onDismiiss: Boolean = false) {

        if(viewModel.recordState.value == true ){
            if(mediaRecorder != null) {
                try {
                    mediaRecorder.stop()
                } catch (e: Exception) {
                    Toast.makeText(fragment.requireContext(), "녹음에 실패했습니다. 한번 더 시도해주세요.", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "stopRecord: 녹음 중지 실패", e)
                }
            }
        }

        mediaRecorder.release()

        if(onDismiiss) {
            audioName = ""
        }
    }


    fun playRecord() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer()

        try {
            val file = File(audioName)
            if (!file.exists() || file.length() == 0L) {
                Log.d(TAG, "파일이 존재하지 않거나 크기가 0입니다.")
                return
            }

            mediaPlayer?.apply {
                setDataSource(file.absolutePath)
                prepare()
                start()
            }

        } catch (e: Exception) {
            Log.d(TAG, "playRecord 오류: $e")
            e.printStackTrace()
        }
    }

}