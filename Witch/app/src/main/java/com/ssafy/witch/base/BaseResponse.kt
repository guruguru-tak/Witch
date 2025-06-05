package com.ssafy.witch.base

import com.google.gson.annotations.SerializedName
import com.ssafy.witch.data.model.response.ErrorResponse

// 반복되는 리스폰스 내용 중복을 줄이기 위해 사용. 리스폰스 데이터 클래스를 만들때 상속해서 사용합니다.
open class BaseResponse<T>(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("data") val data: T? = null,
    @SerializedName("error") val error: ErrorResponse
)
