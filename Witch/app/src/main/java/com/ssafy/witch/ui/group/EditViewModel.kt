package com.ssafy.witch.ui.group

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.GroupInfo
import com.ssafy.witch.data.model.dto.ObjectKey
import com.ssafy.witch.data.model.dto.User
import com.ssafy.witch.data.model.response.ErrorResponse
import com.ssafy.witch.data.model.response.PresignedUrl
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.groupService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.s3Service
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.userService
import com.ssafy.witch.ui.ContentActivity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


private const val TAG = "GroupCreateViewModel"
class EditViewModel : ViewModel() {
    val _errorMessage = MutableLiveData<String>("")
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _file = MutableLiveData<Uri?>()
    val file: LiveData<Uri?>
        get() = _file

    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String>
        get() = _groupName


    fun setFile(file: Uri?) {
        _file.value = file
    }

    fun deleteFile() {
        _file.value = null
    }

    fun setGroupName(groupName: String) {
        _groupName.value = groupName
    }

    fun setName(name: String) {
        _name.value = name
    }

    suspend fun uploadImage(screen: String, context: ContentActivity, groupId: String = "") {
        runCatching {
            var response=true
            var presignedUrl = PresignedUrl("",null)

            if (file.value != null) {
                presignedUrl = getPresignedUrl(screen)
                response = uploadImgS3(presignedUrl.presignedUrl, context)
            }
            if ( file.value == null || response) {
                when (screen) {
                    "edit" -> editGroupImage(groupId,presignedUrl.objectKey, context)
                    "create" -> createGroup(groupName.value!!, presignedUrl.objectKey, context)
                    "profile" -> editProfileImage(presignedUrl, context)
                }
            } else {
                throw Exception("업로드 실패")
            }
        }.onFailure {
            it.printStackTrace()
        }
    }



    suspend fun getPresignedUrl(screen: String): PresignedUrl {
        return suspendCoroutine { continuation ->
            viewModelScope.launch {
                runCatching {
                    when (screen) {
                        "edit" -> groupService.getPresignedUrl("filename.png")
                        "create" -> groupService.getPresignedUrl("filename.png")
                        "profile" ->
                            userService.getPresignedUrl("filename.png")

                        else -> {
                            throw Exception("잘못된 screen")
                        }
                    }
                }.onSuccess {
                    if (it.isSuccessful) {
                        if (it.body()?.success==true) {
                            val presignedUrl = it.body()?.data!!
                            Log.d(TAG, "getPresignedUrl: ${presignedUrl.presignedUrl}")
                            Log.d(TAG, "getPresignedUrl: ${presignedUrl.objectKey}")

                            continuation.resume(presignedUrl)
                        }
                    } else if(it.code() == 400) {
                        Log.d(TAG, "getPresignedUrl: ${it.errorBody()?.string()}")

                        val data = Gson().fromJson(it.errorBody()?.string(), ErrorResponse::class.java)


                        continuation.resumeWithException(Exception(data.errorMessage))
                    }
                }.onFailure {
                    it.printStackTrace()
                    continuation.resumeWithException(it)
                }
            }
        }
    }


    suspend fun uploadImgS3(presignedUrl: String, context: Context): Boolean {
        return suspendCoroutine { continuation ->
            viewModelScope.launch {
                runCatching {
                    Log.d(TAG, "uploadImgS3: ${file.value}")
                    val file = getFileFromUri(context, file.value)
                    val requestBody = file.asRequestBody("image/jpeg".toMediaType())

                    s3Service.uploadFile(presignedUrl, requestBody)
                }.onSuccess {
                    if (it.isSuccessful) {
                        Log.d(TAG, "uploadImgS3: 성공><")
                        continuation.resume(true)
                    } else {
                        Log.d(TAG, "uploadImgS3: 실패><")
                        continuation.resumeWithException(Exception("업로드 실패"))
                    }
                }.onFailure {
                    it.printStackTrace()
                    continuation.resumeWithException(it)
                }
            }
        }
    }

    fun createGroup(groupName: String, groupImageObjectKey: String?, context: ContentActivity) {
        viewModelScope.launch {
            runCatching {
                groupService.createGroup(GroupInfo(groupName, groupImageObjectKey))
            }.onSuccess {
                if (it.isSuccessful) {
                    context.finish()
                }else if(it.code() == 400){
                    it.errorBody()?.let { body ->
                        val data = Gson().fromJson(body.string(), BaseResponse::class.java)
                        _errorMessage.value = data.error.errorMessage
                    }
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun editGroupImage(groupId:String,objectKey: String?, context: ContentActivity) {
        viewModelScope.launch {
            runCatching {
                groupService.editGroupImage(groupId,ObjectKey(objectKey))
            }.onSuccess {
                if (it.isSuccessful) {
                    Log.d(TAG, "editGroupImage: 성공><")
                    context.finish()
                } else if(it.code() == 400) {
                    it.errorBody()?.let { body ->
                        val data = Gson().fromJson(body.string(), BaseResponse::class.java)
                        _errorMessage.value = data.error.errorMessage
                    }
                    Log.d(TAG, "editGroupImage: ${it.errorBody()}")
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun editGroupName(groupId: String,groupName: String, context: ContentActivity) {
        viewModelScope.launch {
            runCatching {
                groupService.editGroupName(groupId,groupName)
            }.onSuccess {
                if (it.isSuccessful) {
                    context.finish()
                    Log.d(TAG, "editGroupName: 성공><")
                } else if(it.code() == 400) {
                    it.errorBody()?.let { body ->
                        val data = Gson().fromJson(body.string(), BaseResponse::class.java)
                        _errorMessage.value = data.error.errorMessage
                    }
                    Log.d(TAG, "editGroupName: ${it.errorBody()}")
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun editProfileImage(presignedUrl: PresignedUrl, context: ContentActivity) {
        viewModelScope.launch {
            runCatching {
                userService.editProfileImage(ObjectKey(presignedUrl.objectKey))
            }.onSuccess {
                if (it.isSuccessful) {
                    val user=ApplicationClass.sharedPreferencesUtil.getUser()
                    ApplicationClass.sharedPreferencesUtil.addUser(User(user.userId,user.email,user.nickname,presignedUrl.presignedUrl))

                    context.finish()
                } else if(it.code() == 400) {
                    it.errorBody()?.let { body ->
                        val data = Gson().fromJson(body.string(), BaseResponse::class.java)
                        _errorMessage.value = data.error.errorMessage
                    }
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun editProfileName(name: String, context: ContentActivity) {
        viewModelScope.launch {
            runCatching {
                userService.editProfileName(name)
            }.onSuccess {
                if (it.isSuccessful) {
                    val user=ApplicationClass.sharedPreferencesUtil.getUser()
                    ApplicationClass.sharedPreferencesUtil.addUser(User(user.userId,user.email,name,user.profileImageUrl))
                    context.finish()
                    Log.d(TAG, "editProfileName: 성공><")
                } else if(it.code() == 400) {
                    it.errorBody()?.let { body ->
                        val data = Gson().fromJson(body.string(), BaseResponse::class.java)
                        _errorMessage.value = data.error.errorMessage
                    }
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun getFileFromUri(context: Context, uri: Uri?): File {
        val inputStream = uri?.let { context.contentResolver.openInputStream(it) }
        val file = File(context.cacheDir, "upload_image.jpg") // 임시 저장소에 파일 생성
        inputStream.use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }
        return file
    }

    fun checkDupl(name:String, screen: String, context: ContentActivity){
        viewModelScope.launch {
            runCatching {
                when(screen) {
                    "group" -> groupService.checkGroupName(name)
                    "profile" -> userService.checkNicknameUnique(name)
                    else -> {
                        throw Exception("잘못된 screen")
                    }
                }
            }.onSuccess {
                if (it.isSuccessful) {
                    Toast.makeText(context, "사용 가능한 이름입니다.", Toast.LENGTH_SHORT).show()
                }else if(it.code() == 400){
                    it.errorBody()?.let { body ->
                        val data = Gson().fromJson(body.string(), BaseResponse::class.java)
                        _errorMessage.value = data.error.errorMessage
                    }
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }






}