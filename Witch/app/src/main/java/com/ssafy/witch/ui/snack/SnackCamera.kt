package com.ssafy.witch.ui.snack

import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.ssafy.witch.R
import com.ssafy.witch.databinding.DialogSnackRecordBinding
import com.ssafy.witch.databinding.FragmentSnackCreateBinding
import java.io.File


private const val TAG = "SnackCamera"
class SnackCamera(private val fragment: Fragment,
                  private val viewModel: SnackCreateViewModel,
                  private val requestPermissionLauncher: ActivityResultLauncher<String>
) {
    private var cameraName = ""
    private val viewFinder: PreviewView by lazy {
        fragment.requireView().findViewById(R.id.snack_create_fg_pv_image)
    }
    private val binding: FragmentSnackCreateBinding by lazy {
        FragmentSnackCreateBinding.inflate(fragment.layoutInflater)
    }

    private lateinit var cameraProvider: ProcessCameraProvider

    private lateinit var imageCapture: ImageCapture
    private var isFront = false

    val cameraProviderFuture = ProcessCameraProvider.getInstance(fragment.requireActivity())


    fun startCamera() {
        cameraProvider= cameraProviderFuture.get()
        cameraProviderFuture.addListener({

            try {
                bindCamera()

            } catch(e: Exception) {
                Log.e(TAG, "카메라 불가", e)
            }

        }, ContextCompat.getMainExecutor(fragment.requireContext()))
    }


    fun stopCamera() {
        cameraProviderFuture.addListener({
            cameraProviderFuture.get().unbindAll()
        }, ContextCompat.getMainExecutor(fragment.requireContext()))
    }

    fun initCamera() {
        if (ActivityCompat.checkSelfPermission(fragment.requireContext(),
                android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    fun toggleCamera(){
        isFront = !isFront
        bindCamera()
    }

    private fun bindCamera() {
        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

        imageCapture = ImageCapture.Builder().build()

        val cameraSelector = if (isFront) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }

        cameraProvider.unbindAll()

        cameraProvider.bindToLifecycle(
            fragment, cameraSelector, preview, imageCapture)
    }

    fun takePicture() {

        val imageCapture = imageCapture ?: return

        val path = fragment.requireContext().getExternalFilesDir("/")?.absolutePath

        val fileName= "$path/${System.currentTimeMillis()}.jpg"

        val file = File(fileName)



        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(fragment.requireActivity()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    stopCamera()
                    binding.snackCreateFgIvImage.setImageURI(fileName.toUri())
                    viewModel.setPhoto(fileName.toUri())
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "사진 저장 실패", exception)
                }
            })
    }
}